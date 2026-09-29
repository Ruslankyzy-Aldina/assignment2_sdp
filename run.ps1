param([string]$Family = 'road', [switch]$Test)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Force 'target/classes' | Out-Null
    $sources = @(Get-ChildItem 'src/main/java' -Filter '*.java' -Recurse | ForEach-Object { $_.FullName })
    & javac -encoding UTF-8 -Xlint:all -Werror -d target/classes @sources
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
    if ($Test) {
        New-Item -ItemType Directory -Force 'target/test-classes' | Out-Null
        $tests = @(Get-ChildItem 'src/test/java' -Filter '*.java' -Recurse | ForEach-Object { $_.FullName })
        & javac -encoding UTF-8 -Xlint:all -Werror -cp target/classes -d target/test-classes @tests
        if ($LASTEXITCODE -ne 0) { throw 'Test compilation failed' }
        & java -cp 'target/classes;target/test-classes' org.example.AssignmentTests
    } else {
        & java -cp target/classes org.example.Main $Family
    }
    if ($LASTEXITCODE -ne 0) { throw 'Execution failed' }
} finally { Pop-Location }

# Part A: direct creation

Domain: transporting museum artifact crates between exhibition sites. Road,
air and sea shipments have different capacity, handling and manifest protocols.

Problems visible in `DirectDispatch`:

1. Both operations depend on concrete planners and must change for each new family.
2. Family-selection branches are duplicated across dispatch and replacement quotes.
3. `accidentalMix` compiles even though an air label carries road planning assumptions.
4. The caller owns construction and coordination, leaving no reusable validation workflow.

This intentionally small baseline is preserved in history and in the legacy package.

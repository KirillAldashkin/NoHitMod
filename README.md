# No Hit mod

Adds a *no-hit challenge* to your game: any allied
block or unit is destroyed by any kind of damage.

It works by pinning HP of all `Sharded` blocks and units at almost[^1] 0.

[^1]: Actual value is an epsilon for IEEE-754 
`float`, which is equal to $2^{-149}$.
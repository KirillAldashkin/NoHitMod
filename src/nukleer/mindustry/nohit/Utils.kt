package nukleer.mindustry.nohit

const val epsilon: Float = 1.4E-45F

fun Float.bitDec(): Float {
    if (isNaN() || isInfinite()) return this
    if (this == 0f) return -epsilon
    if (this > 0f) return Float.fromBits(toRawBits() - 1)
    /* this < 0 */ return Float.fromBits(toRawBits() + 1)
}

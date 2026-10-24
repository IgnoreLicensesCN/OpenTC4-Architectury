package thaumcraft.common.entities.golems.properties;

//should always be singleton
public abstract class ExtraPropertiesMarker<T> {
    public abstract T safetyCheck(T value);
//            public static abstract class IntExtraPropertiesMarker extends ExtraPropertiesMarker<Integer>{
//                @Override
//                @Deprecated(forRemoval = true)
//                public Integer safetyCheckAndFix(Integer value){
//                    return safetyCheckInt(value);
//                }
//                public abstract int safetyCheckInt(int value);
//            }
//            public static abstract class LongExtraPropertiesMarker extends ExtraPropertiesMarker<Long>{
//                @Override
//                @Deprecated(forRemoval = true)
//                public Long safetyCheckAndFix(Long value){
//                    return safetyCheckLong(value);
//                }
//                public abstract long safetyCheckLong(long value);
//            }
//            public static abstract class ByteExtraPropertiesMarker extends ExtraPropertiesMarker<Byte>{
//                @Override
//                @Deprecated(forRemoval = true)
//                public Byte safetyCheckAndFix(Byte value){
//                    return safetyCheckByte(value);
//                }
//                public abstract byte safetyCheckByte(byte value);
//            }
//            public static abstract class CharExtraPropertiesMarker extends ExtraPropertiesMarker<Character>{
//                @Override
//                @Deprecated(forRemoval = true)
//                public Character safetyCheckAndFix(Character value){
//                    return safetyCheckChar(value);
//                }
//                public abstract char safetyCheckChar(char value);
//            }
//            public static abstract class FloatExtraPropertiesMarker extends ExtraPropertiesMarker<Float>{
//                @Override
//                @Deprecated(forRemoval = true)
//                public Float safetyCheckAndFix(Float value){
//                    return safetyCheckFloat(value);
//                }
//                public abstract float safetyCheckFloat(float value);
//            }
//            public static abstract class DoubleExtraPropertiesMarker extends ExtraPropertiesMarker<Double>{
//                @Override
//                @Deprecated(forRemoval = true)
//                public Double safetyCheckAndFix(Double value){
//                    return safetyCheckDouble(value);
//                }
//                public abstract double safetyCheckDouble(double value);
//            }
//            public static abstract class ShortExtraPropertiesMarker extends ExtraPropertiesMarker<Short>{
//                @Override
//                @Deprecated(forRemoval = true)
//                public Short safetyCheckAndFix(Short value){
//                    return safetyCheckShort(value);
//                }
//                public abstract short safetyCheckShort(short value);
//            }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.fhz4j;

import net.sf.fhz4j.fht.FhtProperty;

/**
 *
 * @author aploese
 */
public interface FhzProperty {

    String getName();

    String getLabel();

    final static class Util {

        public final static FhzProperty fromLabel(FhzDeviceTypes deviceType, String propertyLabel) {
            switch (deviceType) {
                case FHT_80B:
                case FHT_8:
                    return FhtProperty.fromLabel(propertyLabel);
                default:
                    throw new RuntimeException(String.format("Dont know to hande property %s of : %s", propertyLabel, deviceType));
            }
        }

        public static String[] getFhzPropertyLabelsOf(FhzDeviceTypes deviceType) {
            return getFhzPropertyLabelsOf(getFhzPropertiesOf(deviceType));
        }

        public static String[] getFhzPropertyLabelsOf(FhzProperty[] fhzProperties) {
            String[] result = new String[fhzProperties.length];
            for (int i = 0; i < fhzProperties.length; i++) {
                result[i] = fhzProperties[i].getLabel();
            }
            return result;
        }

        public static FhzProperty[] getFhzPropertiesOf(FhzDeviceTypes deviceType) {
            switch (deviceType) {
                case FHT_80B:
                case FHT_8:
                    return FhtProperty.getFhzPropertiesOf(deviceType);
                default:
                    throw new RuntimeException(String.format("Dont know to hande deviceType %s", deviceType));
            }
        }
    }
}

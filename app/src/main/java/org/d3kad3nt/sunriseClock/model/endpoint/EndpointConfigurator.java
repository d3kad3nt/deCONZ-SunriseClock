package org.d3kad3nt.sunriseClock.model.endpoint;

import androidx.core.util.Pair;

import java.util.List;

public interface EndpointConfigurator {

    /**
     * This returns a definition of the configuration structure of the endpoint.
     *
     * It is a List that contains multiple Pairs.
     * This Pairs have a name and a List. This are Subcategories, e.g Auth, Connections, ...
     * The List inside that pair contain more Pairs. This Pairs are the settings that a Endpoint has.
     * The Settings have a Name and a Type. The Type is used e.g. to generate the interface where the Endpoints get created.
     * @return A definition of the settings that this endpoint can have.
     */
    List<Pair<String,List<Pair<String,SettingType>>>> getSettings();

    /**
     * This Method returns the Data of the current Setting, formatted in the same way as {@link EndpointConfigurator#getSettings()}
     * TODO possible Problem: You have to change the structure of the Endpoint at two points (here and getSettings())
     * @return
     */
    List<Pair<String,List<Pair<String,SettingValue>>>> getData();

    /**
     * This enum contains all possible types for Settings of endpoints.
     */
    enum SettingType {
        String(java.lang.String.class),
        Integer(java.lang.Integer.class),
        Float(java.lang.Float.class),
        Date(java.util.Date.class),
        IP(java.lang.String.class),
        Boolean(java.lang.Boolean.class);

        private final Class type;

        SettingType(Class type) {
            this.type = type;
        }

        public Class getType() {
            return type;
        }
    }

    /**
     * This Class holds the Value of the setting.
     */
    class SettingValue {
        private final SettingType type;
        private final Object value;

        public SettingValue(SettingType type, Object value) {
            if (!type.getType().equals(value.getClass())){
                throw new IllegalArgumentException("The value has the wrong type (value :" + value.getClass() + ", SettingType: " + type.getType());
            }
            this.type = type;
            this.value = value;
        }

        public SettingType getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }
    }
}

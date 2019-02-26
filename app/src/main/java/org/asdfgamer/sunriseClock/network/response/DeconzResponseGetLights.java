package org.asdfgamer.sunriseClock.network.response;

import java.util.ArrayList;

public class DeconzResponseGetLights extends DeconzResponseObject {

    public DeconzResponseGetLights() {
        super();
    }

    private ArrayList<DeconzLight> lights = new ArrayList<>();

    /**
     * One of the lights retrieved from 'lights' endpoint with needed attributes.
     */
    public class DeconzLight {

        private String id;

        private String manufacturer;

        private String name;

        private String modelid;

        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getModelid() {
            return modelid;
        }

        public void setModelid(String modelid) {
            this.modelid = modelid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public ArrayList<DeconzLight> getLights() {
        return lights;
    }
}

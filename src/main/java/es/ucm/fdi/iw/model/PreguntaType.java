package es.ucm.fdi.iw.model;

public enum PreguntaType {
    OPCION_MULTIPLE("multichoice"),
    RESPUESTA_CORTA("shortanswer"),
    TRUE_FALSE("truefalse"),
    RESPUESTA_FOTO("photo");

    public final String name;

    PreguntaType(String name) {
        this.name = name;
    }

    public static PreguntaType byName(String name) {
        for (PreguntaType pt : values()) {
            if (pt.name.equals(name))
                return pt;
        }
        throw new IllegalArgumentException("No such preguntaType: " + name);
    }
}

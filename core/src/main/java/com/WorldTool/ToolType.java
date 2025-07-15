package com.WorldTool;

public enum ToolType {
    BLOCK("Block Editor"),
    PROP("Prop Editor"),
    ITEM("Item Editor"),
    ENTITY("Entity Editor"),
    ANIMATION("Animation Editor"),
    STRUCTURE("Structure Editor"),
    REGION("Region Editor"),
    WORLD("World Editor");

    private final String label;

    ToolType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ToolType fromLabel(String label) {
        for (ToolType type : ToolType.values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown label: " + label);
    }
}
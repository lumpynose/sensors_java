package com.objecteffects.temperature.gui;

class TestGuiLayout {
    private static void runGui() {
        System.out.println("starting test");
        GuiLayout guiLayout = new GuiLayout();

        guiLayout.setup();

//        guiLayout.addLabel();
//        guiLayout.addLabel();
//        guiLayout.addLabel();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                runGui();
                System.out.println("runGui started");
            }
        });
    }
}

package com.zomwi.simidic.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IfoDialog extends JDialog {

    private MainFrame raiz;

    private JTextField cajaNombre;
    private JTextField cajaDescripcion;
    private JTextField cajaAutor;
    private JTextField cajaIdiomaOrigen;
    private JTextField cajaIdiomaDestino;

    private JButton botonAceptar;


    public IfoDialog(MainFrame raiz) {
        super(raiz, true);
        this.raiz = raiz;
        dialogConfig();
        initComponents();
    }

    private void dialogConfig() {
        setTitle("Diccionario");
        setLayout(null);
        setSize(245, 280);
        setLocationRelativeTo(raiz);
        setResizable(false);

    }

    private void initComponents() {
        cajaNombre = new JTextField();
        cajaNombre.setBounds(10, 20, 220, 30);


        cajaDescripcion = new JTextField();
        cajaDescripcion.setBounds(10, 100, 220, 30);

        cajaAutor = new JTextField();
        cajaAutor.setBounds(10, 60, 220, 30);

        cajaIdiomaOrigen = new JTextField();
        cajaIdiomaOrigen.setBounds(10, 140, 220, 30);

        cajaIdiomaDestino = new JTextField();
        cajaIdiomaDestino.setBounds(10, 180, 220, 30);

        botonAceptar = new JButton("Aceptar");
        botonAceptar.setBounds(20, 215, 200, 30);
        botonAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarDatosNucleo();
            }
        });


        // Extra textos
        JLabel textoNombre = new JLabel("Nombre");
        JLabel textoAutor = new JLabel("Autor");
        JLabel textoDescripcion = new JLabel("Descripción");
        JLabel textoIdiomaOrigen = new JLabel("Idioma origen");
        JLabel textoIdiomaDestino = new JLabel("Idioma destino");
        textoNombre.setBounds(10, 5, 65, 25);
        textoAutor.setBounds(10, 45, 65, 25);
        textoDescripcion.setBounds(10, 85, 65, 25);
        textoIdiomaOrigen.setBounds(10, 125, 65, 25);
        textoIdiomaDestino.setBounds(10, 165, 65, 25);
        textoNombre.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        textoAutor.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        textoDescripcion.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        textoIdiomaOrigen.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        textoIdiomaDestino.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));

        add(cajaNombre);
        add(cajaAutor);
        add(cajaDescripcion);
        add(cajaIdiomaOrigen);
        add(cajaIdiomaDestino);
        add(textoNombre);
        add(textoAutor);
        add(textoDescripcion);
        add(textoIdiomaOrigen);
        add(textoIdiomaDestino);

        add(botonAceptar);
    }

    public void enviarDatosNucleo() {
        String nombre = cajaNombre.getText().trim();
        String autor = cajaAutor.getText().trim();
        String descripcion = cajaDescripcion.getText().trim();
        String idiomaOrigen = cajaIdiomaOrigen.getText().trim();
        String idiomaDestino = cajaIdiomaDestino.getText().trim();

        raiz.setDataKernel(nombre, autor, descripcion, idiomaOrigen, idiomaDestino);

        this.setVisible(false);

    }

    public void setData(String name, String author, String description, String langBegin, String langEnd) {
        cajaNombre.setText(name);
        cajaAutor.setText(author);
        cajaDescripcion.setText(description);
        cajaIdiomaOrigen.setText(langBegin);
        cajaIdiomaDestino.setText(langEnd);
    }
}

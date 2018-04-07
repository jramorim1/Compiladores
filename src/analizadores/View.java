package analizadores;
import OperationsAST.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import AST.nodePrograma;

import javax.swing.*;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class View{

	private JFrame frame;
	private JTextField cF;
	private JTextField cO;
	private JButton btnSin;
	private JLabel lblOpes;
	private JButton btnLxico;
	private JButton btnrvore;
	private JButton btnContexto;
	private JButton btnGeraoDeCdigo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public View() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Compilador");
		frame.setBounds(300, 50, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblCaminhoDoCdigo = new JLabel("Caminho do C\u00F3digo Fonte:");
		lblCaminhoDoCdigo.setBounds(23, 55, 138, 14);
		frame.getContentPane().add(lblCaminhoDoCdigo);
		
		JLabel lblCaminhoDoCdigo_1 = new JLabel("Caminho do C\u00F3digo Objeto:");
		lblCaminhoDoCdigo_1.setBounds(23, 86, 138, 14);
		frame.getContentPane().add(lblCaminhoDoCdigo_1);
		
		cF = new JTextField();
		cF.setBounds(171, 52, 348, 20);
		frame.getContentPane().add(cF);
		cF.setColumns(10);
		
		cO = new JTextField();
		cO.setColumns(10);
		cO.setBounds(171, 83, 348, 20);
		frame.getContentPane().add(cO);
		
		btnSin = new JButton("An\u00E1lise Sint\u00E1tica");
		btnSin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				String path = cF.getText();	
				
				Parser parser = new Parser(path);
				nodePrograma p;
				p = parser.parse();
				System.out.println("End.");
				}catch(Exception a){
					System.out.println(a.getMessage());
				}
			}
		});
		btnSin.setBounds(161, 154, 122, 23);
		frame.getContentPane().add(btnSin);
		
		lblOpes = new JLabel("Seleciona at\u00E9 o compilador dever\u00E1 ser executado:");
		lblOpes.setBounds(23, 129, 247, 14);
		frame.getContentPane().add(lblOpes);
		
		btnLxico = new JButton("An\u00E1lise L\u00E9xica");
		btnLxico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnLxico.setBounds(23, 154, 110, 23);
		frame.getContentPane().add(btnLxico);
		
		btnrvore = new JButton("Impress\u00E3o da \u00C1rvore");
		btnrvore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String path = cF.getText();	
					
					Parser parser = new Parser(path);
					nodePrograma p;
					p = parser.parse();
					Printer printer = new Printer();
					printer.print(p);
					System.out.println("End.");
					}catch(Exception a){
						System.out.println(a.getMessage());
					}
				
			}
		});
		btnrvore.setBounds(315, 154, 143, 23);
		frame.getContentPane().add(btnrvore);
		
		btnContexto = new JButton("An\u00E1lise de Contexto");
		btnContexto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				String path = cF.getText();	
				
				Parser parser = new Parser(path);
				nodePrograma p;
				p = parser.parse();
				Printer printer = new Printer();
				Checker checker = new Checker();
				System.out.println("End.");
				}catch(Exception a){
					System.out.println(a.getMessage());
				}
			}
		});
		btnContexto.setBounds(74, 202, 133, 23);
		frame.getContentPane().add(btnContexto);
		
		btnGeraoDeCdigo = new JButton("Gera\u00E7\u00E3o de C\u00F3digo");
		btnGeraoDeCdigo.setBounds(250, 202, 128, 23);
		frame.getContentPane().add(btnGeraoDeCdigo);
	}
}

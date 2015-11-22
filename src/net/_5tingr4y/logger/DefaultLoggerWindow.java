package net._5tingr4y.logger;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import net._5tingray.stingUtils.math.algebra.linear.Vector2f;
import net.miginfocom.swing.MigLayout;

public class DefaultLoggerWindow extends JFrame implements LoggerWindow {
	
	private static final long serialVersionUID = -1975407619715769848L;
	
	private JPanel contentPane;
	private JTextPane textPane;
	private JButton exitButton;
	private Runnable exitCmd;
	
	public DefaultLoggerWindow(String title) {
		this(title, new Vector2f(100, 100), new Vector2f(800, 600));
	}
	
	public DefaultLoggerWindow(String title, Vector2f pos, Vector2f size) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle(title);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				Logger.log(this, Logger.DEBUG, "LoggerWindow closed");
				ev.getWindow().dispose();
			}
		});
		
		setBounds((int) pos.getX(), (int) pos.getY(), (int) size.getX(), (int) size.getY());
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("",
				"[grow][grow][200px:200px:200px][100px:100px:100px]",
				"[grow,fill][]"));

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 0 0 4 1,grow");

		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);

		exitButton = new JButton("Exit Game");
		exitButton.setEnabled(false);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger.log(this, Logger.DEBUG, "Attemting to close application");
				if (exitCmd != null)
					exitCmd.run();
			}
		});
		contentPane.add(exitButton, "cell 3 1,grow");
		
		setVisible(true);
	}
	
	@Override
	public void addMessage(Color color, String message) {
		Document doc = textPane.getDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);
		try {
			StyleConstants.setForeground(def, color);
			StyleConstants.setFontFamily(def, "Monospaced");
			StyleConstants.setFontSize(def, 15);
			doc.insertString(doc.getLength(), message + System.getProperty("line.separator"), def);
		} catch (Exception e) {
		}
	}

	void setExitCommand(Runnable cmd) {
		exitCmd = cmd;
		exitButton.setEnabled(cmd != null);
	}

}
package uk.co.newagedev.brainfuck;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Main implements ActionListener {

	public JFrame frame;
	public JLabel indexView, stackView, loopStackView, brainfuckLabel, outputLabel;
	public JTextArea brainfuck, output;
	public JButton runButton, stepButton;
	public JPanel panel;
	public int index, current;
	public ArrayList<Integer> stack = new ArrayList<Integer>();
	public LinkedList<Integer> loopStack = new LinkedList<Integer>();
	
	public Main() {
		frame = new JFrame();
		frame.setTitle("Brainfuck Interpreter");
		initGUI();
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
	}
	
	public void setIndexLabel() {
		indexView.setText("Index: " + brainfuck.getText().length() + "/" + index);
	}
	
	public void setStackLabel() {
		stackView.setText("Stack: " + stack.toString());
	}
	
	public void setLoopStackLabel() {
		loopStackView.setText("Loop Stack: " + loopStack.toString());
	}
	
	public void initGUI() {
		indexView = new JLabel();
		stackView = new JLabel();
		loopStackView = new JLabel();
		brainfuckLabel = new JLabel("Brainfuck input:");
		brainfuck = new JTextArea(15, 40);
		runButton = new JButton("Run");
		stepButton = new JButton("Step");
		outputLabel = new JLabel("Output:");
		output = new JTextArea(10, 40);
		
		indexView.setLocation(10, 10);
		
		stackView.setLocation(10, 20);
		
		loopStackView.setLocation(10, 30);
		
		brainfuckLabel.setLocation(10, 40);
		
		brainfuck.setLocation(10, 50);
		brainfuck.setLineWrap(true);
		
		runButton.setLocation(30, 100);
		runButton.addActionListener(this);
		runButton.setActionCommand("run");
		
		stepButton.setLocation(60, 100);
		stepButton.addActionListener(this);
		stepButton.setActionCommand("step");
		
		outputLabel.setLocation(10, 120);
		
		output.setLocation(10, 130);
		output.setLineWrap(true);
		output.setEditable(false);
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(indexView);
		panel.add(stackView);
		panel.add(loopStackView);
		panel.add(brainfuckLabel);
		panel.add(brainfuck);
		panel.add(runButton);
		panel.add(stepButton);
		panel.add(outputLabel);
		panel.add(output);
	}
	
	public void addToOutput(char c) {
		output.setText(output.getText() + c);
	}
	
	public void runBrainfuck() {
		initBrainfuck();
		while (current != brainfuck.getText().length()) {
			step();
		}	
	}
	
	public void initBrainfuck() {
		current = 0;
		stack.clear();
		stack.add(0);
		loopStack.clear();
		index = 0;
		output.setText("");
	}
	
	public void step() {
		String text = brainfuck.getText();
		switch(text.charAt(current)) {
		case '>':
			index += 1;
			if (index == stack.size())
				stack.add(0);
			break;
		case '<':
			index -= 1;
			if (index < 0)
				index = 0;
			break;
		case '+':
			stack.set(index, stack.get(index) + 1);
			break;
		case '-':
			stack.set(index, stack.get(index) - 1);
			if (stack.get(index) < 0)
				stack.set(index, 0);
			break;
		case '[':
			loopStack.addLast(current);
			break;
		case ']':
			if (loopStack.isEmpty())
				break;
			current = loopStack.getLast();
			if (stack.get(index) <= 1)
				loopStack.removeLast();
			break;
		case '.':
			addToOutput((char) (int) stack.get(index));
			break;
		}
		setIndexLabel();
		setLoopStackLabel();
		setStackLabel();
		current += 1;
	}
	
	public static void main(String[] args) {
		new Main();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand() == "run") {
			runBrainfuck();
		} else if (ae.getActionCommand() == "step") {
			if (stack.size() == 0 || current == brainfuck.getText().length()) {
				initBrainfuck();
			}
			step();
		}
	}
}

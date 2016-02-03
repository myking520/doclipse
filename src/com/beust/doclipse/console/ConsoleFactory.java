package com.beust.doclipse.console;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class ConsoleFactory implements IConsoleFactory {
	private final static IConsoleManager consoleManager= ConsolePlugin.getDefault().getConsoleManager();
	private final  static MessageConsole console=new MessageConsole("", null);
	@Override
	public void openConsole() {
		IConsole[] existing = consoleManager.getConsoles();
		boolean exists = false;
		for (int i = 0; i < existing.length; i++) {
			if (console == existing[i])
				exists = true;
		}
		if (!exists)
			consoleManager.addConsoles(new IConsole[] {console});
		consoleManager.showConsoleView(console);
	}
	public final static MessageConsole getMessageConsole(){
		
		return console;
	}
}

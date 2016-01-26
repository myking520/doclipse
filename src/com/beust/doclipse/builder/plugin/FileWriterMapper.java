package com.beust.doclipse.builder.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.generama.Plugin;
import org.generama.WriterMapper;
import org.generama.defaults.Outcome;

public class FileWriterMapper implements WriterMapper {
	private String outDir;
	private String outFile;
	
	public FileWriterMapper(String outDir, String outFile) {
		super();
		this.outDir = outDir;
		this.outFile = outFile;
	}
	@Override
	public Outcome getOutcome(Object arg0, Plugin plugin) throws IOException {
		  File dir = new File(outDir);
		  if(!dir.exists()){
			  dir.mkdirs();
		  }
		  File out = new File(dir, outFile);
		  OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(out), plugin.getEncoding());
          return new Outcome(writer, out.toURL());
	}

}

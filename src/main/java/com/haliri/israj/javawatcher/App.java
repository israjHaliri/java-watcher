package com.haliri.israj.javawatcher;

import com.haliri.israj.javawatcher.entity.IncomingFile;
import com.haliri.israj.javawatcher.service.IncomingFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@SpringBootApplication
public class App implements CommandLineRunner {

	@Autowired
	private IncomingFileService incomingFileService;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		initWatcher();
	}

	public static Logger getLogger(Object object) {
		return LoggerFactory.getLogger(object.getClass());
	}

	private void initWatcher() {
		try {

			WatchService watcher = FileSystems.getDefault().newWatchService();

			//change this part to your dir
			Path dir = Paths.get("/tmp");
			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

			App.getLogger(App.class).info("Watch service registered for directory : {}", dir.getFileName());

			WatchKey key;
			while ((key = watcher.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {

					WatchEvent.Kind<?> kind = event.kind();

					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();

					if (kind == ENTRY_CREATE) {
						App.getLogger(App.class).info("NEW FILE ADDED : {}", fileName);
						incomingFileService.processFile(fileName.getFileName().toString());
					}
				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (IOException | InterruptedException ex) {
			System.err.println(ex);
		}
	}
}

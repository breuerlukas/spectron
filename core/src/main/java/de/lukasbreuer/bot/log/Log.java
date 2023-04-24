package de.lukasbreuer.bot.log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public final class Log extends Logger {
  public static Log create(String name, String path) throws Exception {
    var consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(LogFormat.create(LogFormat.FormatType.CONSOLE));
    consoleHandler.setLevel(Level.ALL);
    var fileHandler = new FileHandler(buildLogFilePath(path));
    fileHandler.setFormatter(LogFormat.create(LogFormat.FormatType.FILE));
    var log = new Log(name, consoleHandler, fileHandler);
    log.setLevel(Level.ALL);
    log.addHandler(consoleHandler);
    log.addHandler(fileHandler);
    Runtime.getRuntime().addShutdownHook(new Thread(log::close));
    return log;
  }

  private static String buildLogFilePath(String basePath) {
    var logPath = System.getProperty("user.dir") + basePath +
      new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date()) + ".log";
    var logFile = new File(logPath);
    if (!logFile.getParentFile().exists()) {
      logFile.getParentFile().mkdirs();
    }
    return logPath;
  }

  private final ConsoleHandler consoleHandler;
  private final FileHandler fileHandler;

  private Log(String name, ConsoleHandler consoleHandler, FileHandler fileHandler) {
    super(name, null);
    this.consoleHandler = consoleHandler;
    this.fileHandler = fileHandler;
  }

  @Override
  public void log(LogRecord record) {
    super.log(formatRecord(record));
  }

  public void consoleLog(Level level, String message) {
    restrictedLog(consoleHandler, new LogRecord(level, message));
  }

  public void fileLog(Level level, String message) {
    restrictedLog(fileHandler, new LogRecord(level, message));
  }

  private void restrictedLog(Handler handler, LogRecord record) {
    handler.publish(formatRecord(record));
  }

  private LogRecord formatRecord(LogRecord record) {
    record.setMessage("[" + getName().toUpperCase() + "] " + record.getMessage());
    return record;
  }

  public void close() {
    for (var handler : getHandlers()) {
      handler.close();
    }
  }
}

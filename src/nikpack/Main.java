package nikpack;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class Main {

    public static void main(String[] args) {

        try {

            // создаем сервис отслеживания
            WatchService watcher = FileSystems.getDefault().newWatchService();

            // будем отслеживать каталог watchthis
            Path path = Paths.get("/home/sa/IdeaProjects/WatchService/src/watchthis");
            if (Files.notExists(path))
                Files.createDirectory(path);

            // получаем WatchKey
            WatchKey watchKey = path.register(watcher,
                    ENTRY_CREATE,
                    ENTRY_DELETE,
                    ENTRY_MODIFY);

            stop_watch:
            for(;;) {
                WatchKey key;
                key = watcher.take();

                for(WatchEvent<?> event: key.pollEvents()) {
                    WatchEvent.Kind<?> eventKind = event.kind();
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path entryPath = pathEvent.context();

                    switch (eventKind.toString()) {
                        case "ENTRY_CREATE": {
                            System.out.println("Создан файл: " + entryPath);
                            break;
                        }
                        case "ENTRY_DELETE": {
                            System.out.println("Удален файл: " + entryPath);
                            break;
                        }
                        case "ENTRY_MODIFY": {
                            System.out.println("Изменен файл: " + entryPath);
                            break;
                        }
                        default:
                            break;
                    }

                    if (entryPath.getFileName().toString().equals("exit.txt")) {
                        System.out.println("Вазафак");
                        break stop_watch;
                    }
                }

                // сброс ключа, чтобы получить следующие события
                if (!key.reset())
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.nikhil.mediaSequencerJava.config;
import com.nikhil.mediaSequencerJava.entities.Media;
import com.nikhil.mediaSequencerJava.enums.MediaType;
import com.nikhil.mediaSequencerJava.entities.MediaWindow;
import com.nikhil.mediaSequencerJava.entities.WindowMedia;
import com.nikhil.mediaSequencerJava.repository.MediaRepository;
import com.nikhil.mediaSequencerJava.repository.MediaWindowRepository;
import com.nikhil.mediaSequencerJava.repository.WindowMediaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(
            MediaRepository mediaRepository,
            MediaWindowRepository mediaWindowRepository,
            WindowMediaRepository windowMediaRepository
    ) {
        return args -> {

            if (mediaWindowRepository.count() > 0) {
                return;
            }

            Media m1 = new Media(
                    "M1", "Nature Image", MediaType.IMAGE,
                    "https://picsum.photos/id/1015/1280/720",
                    5000L
            );

            Media m2 = new Media(
                    "M2", "Demo Video", MediaType.VIDEO,
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    5000L
            );

            Media m3 = new Media(
                    "M3", "City Image", MediaType.IMAGE,
                    "https://picsum.photos/id/1016/1280/720",
                    5000L
            );

            Media m4 = new Media(
                    "M4", "Blank Screen", MediaType.BLANK,
                    null,
                    5000L
            );

            mediaRepository.save(m1);
            mediaRepository.save(m2);
            mediaRepository.save(m3);
            mediaRepository.save(m4);

            MediaWindow window1 =
                    mediaWindowRepository.save(
                            new MediaWindow("Window 1")
                    );

            MediaWindow window2 =
                    mediaWindowRepository.save(
                            new MediaWindow("Window 2")
                    );

            MediaWindow window3 =
                    mediaWindowRepository.save(
                            new MediaWindow("Window 3")
                    );

            MediaWindow window4 =
                    mediaWindowRepository.save(
                            new MediaWindow("Window 4")
                    );

            // Window 1 list: M1 -> M2 -> M3 -> M4

            windowMediaRepository.save(
                    new WindowMedia(window1, m1, 1)
            );

            windowMediaRepository.save(
                    new WindowMedia(window1, m2, 2)
            );

            windowMediaRepository.save(
                    new WindowMedia(window1, m3, 3)
            );

            windowMediaRepository.save(
                    new WindowMedia(window1, m4, 4)
            );

            // Window 2 list: M2 -> M3 -> M4 -> M1

            windowMediaRepository.save(
                    new WindowMedia(window2, m2, 1)
            );

            windowMediaRepository.save(
                    new WindowMedia(window2, m3, 2)
            );

            windowMediaRepository.save(
                    new WindowMedia(window2, m4, 3)
            );

            windowMediaRepository.save(
                    new WindowMedia(window2, m1, 4)
            );

            // Window 3 list: M3 -> M4 -> M1 -> M2

            windowMediaRepository.save(
                    new WindowMedia(window3, m3, 1)
            );

            windowMediaRepository.save(
                    new WindowMedia(window3, m4, 2)
            );

            windowMediaRepository.save(
                    new WindowMedia(window3, m1, 3)
            );

            windowMediaRepository.save(
                    new WindowMedia(window3, m2, 4)
            );

            // Window 4 list: M4 -> M1 -> M2 -> M3

            windowMediaRepository.save(
                    new WindowMedia(window4, m4, 1)
            );

            windowMediaRepository.save(
                    new WindowMedia(window4, m1, 2)
            );

            windowMediaRepository.save(
                    new WindowMedia(window4, m2, 3)
            );

            windowMediaRepository.save(
                    new WindowMedia(window4, m3, 4)
            );
        };
    }
}
package demo.dashboard.service;

import demo.dashboard.config.DashboardProps;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardDataService {
    private final DashboardProps dashboardProps;
    private final ActuatorDataService actuatorDataService;

    private LocalDateTime start;
    private JFrame frame = new JFrame("header");
    private JPanel panel = new JPanel();


    @PostConstruct
    private void init() {
        start = LocalDateTime.now();

        frame.setLayout(new GridBagLayout());
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setTitle("Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setPreferredSize(new Dimension(600, 600));
        panel.setLayout(new GridBagLayout());
        panel.setBorder(LineBorder.createBlackLineBorder());

        GridBagConstraints frameConstraints = new GridBagConstraints();
        frameConstraints.gridx = 0;
        frameConstraints.gridy = 1;
        frameConstraints.weighty = 1;

        frame.add(panel, frameConstraints);
        frame.pack();
        frame.setVisible(true);
    }


    @Scheduled(fixedDelay = 1000)
    public void run() {
        CompletableFuture<String> messageCenterDataFuture = actuatorDataService.getData(dashboardProps.getMessageCenter());
        CompletableFuture<String> mrAnalystDataFuture = actuatorDataService.getData(dashboardProps.getMrAnalyst());
        CompletableFuture<String> policeDataFuture = actuatorDataService.getData(dashboardProps.getPolice());

        CompletableFuture<List<String>> features = futureOfList(List.of(messageCenterDataFuture, mrAnalystDataFuture, policeDataFuture));

        LocalDateTime current = LocalDateTime.now();
        Duration between = Duration.between(start, current);
        long millis = between.toMillis();
        String timeDif = getFormattedTime(millis);

//        features.join(); //log error to console
        panel.removeAll();
        panel.add(new JLabel(String.format("time has passed %s", timeDif)), getGridBagConstraints(0));
        features.thenAccept(data -> {

            for (int i = 0; i < data.size(); i++){
                panel.add(new JLabel(data.get(i)), getGridBagConstraints(i + 1));
            }
            panel.updateUI();
        });
    }

    private GridBagConstraints getGridBagConstraints(int gridY){
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = gridY;
        labelConstraints.insets = new Insets(10, 10, 10, 10);
        return labelConstraints;
    }

    private String getFormattedTime(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public CompletableFuture<List<String>> futureOfList(List<CompletableFuture<String>> listOfFuture){
       return CompletableFuture.allOf(listOfFuture.toArray(new CompletableFuture[0]))
                .thenApply(v -> listOfFuture.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }

}

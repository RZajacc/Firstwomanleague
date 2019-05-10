package org.rafalzajac.web.fileProcessing;


import lombok.Data;
import org.rafalzajac.domain.Player;
import org.rafalzajac.domain.Team;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class ScoutFileProcess {

    private Path scoutFilePath;

    public ScoutFileProcess(Path scoutFilePath) {
        this.scoutFilePath = scoutFilePath;
    }

    public List<String> getHomeTeamData() throws Exception{

    List<String> lines = Files.readAllLines(scoutFilePath, Charset.forName("ISO-8859-1"));
    List<String> data = new LinkedList<>();

        for (String line : lines) {
            String gameEventsPattern = "([a*])([0-9]+[0-9])([A-Z])+([A-Z])([#+!-=/])";
            Pattern statPattern = Pattern.compile(gameEventsPattern);
            Matcher matcher = statPattern.matcher(line);
            if(matcher.find()) {
                data.add(matcher.group(1) + matcher.group(2) + matcher.group(3) + matcher.group(4) + matcher.group(5));
                if(matcher.group(3).equals("E") && matcher.group(5).equals("#"))

                    data.add("PERFEKCYJNE PRZYJECIE");
            }
        }






    return data;
}

}

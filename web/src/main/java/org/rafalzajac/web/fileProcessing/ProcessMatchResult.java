package org.rafalzajac.web.fileProcessing;

import org.rafalzajac.domain.Match;
import org.rafalzajac.domain.Team;
import org.rafalzajac.service.MatchService;
import org.rafalzajac.service.TeamService;

import java.util.List;
import java.util.Optional;


public class ProcessMatchResult {

    private TeamService teamService;
    private MatchService matchService;

    public ProcessMatchResult(TeamService teamService, MatchService matchService) {
        this.teamService = teamService;
        this.matchService = matchService;
    }

    public void addMatchResult (Match match) {

        Optional<Match> selected = matchService.getMatchById(match.getId());
        if(selected.isPresent()) {
            Match matchToUpdate = selected.get();

            //Sets won by both teams
            matchToUpdate.getMatchResult().setHomeTeamSetsWon(match.getMatchResult().getHomeTeamSetsWon());
            matchToUpdate.getMatchResult().setAwayTeamSetsWon(match.getMatchResult().getAwayTeamSetsWon());

            //Home team each set score
            matchToUpdate.getMatchResult().setHomeTeamSet1Score(match.getMatchResult().getHomeTeamSet1Score());
            matchToUpdate.getMatchResult().setHomeTeamSet2Score(match.getMatchResult().getHomeTeamSet2Score());
            matchToUpdate.getMatchResult().setHomeTeamSet3Score(match.getMatchResult().getHomeTeamSet3Score());
            matchToUpdate.getMatchResult().setHomeTeamSet4Score(match.getMatchResult().getHomeTeamSet4Score());
            matchToUpdate.getMatchResult().setHomeTeamSet5Score(match.getMatchResult().getHomeTeamSet5Score());

            //Away team each set score
            matchToUpdate.getMatchResult().setAwayTeamSet1Score(match.getMatchResult().getAwayTeamSet1Score());
            matchToUpdate.getMatchResult().setAwayTeamSet2Score(match.getMatchResult().getAwayTeamSet2Score());
            matchToUpdate.getMatchResult().setAwayTeamSet3Score(match.getMatchResult().getAwayTeamSet3Score());
            matchToUpdate.getMatchResult().setAwayTeamSet4Score(match.getMatchResult().getAwayTeamSet4Score());
            matchToUpdate.getMatchResult().setAwayTeamSet5Score(match.getMatchResult().getAwayTeamSet5Score());
            updateTeamStats(matchToUpdate);
            matchService.addMatch(matchToUpdate);
        }


    }


    public void updateTeamStats(Match match) {

        Team homeTeam = match.getTeams().get(0);
        Team awayTeam = match.getTeams().get(1);

        if(  (Integer.parseInt(match.getMatchResult().getHomeTeamSetsWon()) != 0) || (Integer.parseInt(match.getMatchResult().getAwayTeamSetsWon()) != 0) )  {

            //Setting matches played for both teams
            homeTeam.getTeamStats().setMatchPlayed(homeTeam.getTeamStats().getMatchPlayed() + 1);
            awayTeam.getTeamStats().setMatchPlayed(awayTeam.getTeamStats().getMatchPlayed() + 1);

            //Setting sets won and lost for both teams
            homeTeam.getTeamStats().setSetsWon(homeTeam.getTeamStats().getSetsWon() + Integer.parseInt(match.getMatchResult().getHomeTeamSetsWon()));
            homeTeam.getTeamStats().setSetsLost(homeTeam.getTeamStats().getSetsLost() + Integer.parseInt(match.getMatchResult().getAwayTeamSetsWon()));
            awayTeam.getTeamStats().setSetsWon(awayTeam.getTeamStats().getSetsWon() + Integer.parseInt(match.getMatchResult().getAwayTeamSetsWon()));
            awayTeam.getTeamStats().setSetsLost(awayTeam.getTeamStats().getSetsLost() + Integer.parseInt(match.getMatchResult().getHomeTeamSetsWon()));

            //Setting sets ratio for home team
            if (homeTeam.getTeamStats().getSetsLost() == 0) {
                homeTeam.getTeamStats().setSetRatio(homeTeam.getTeamStats().getSetsWon());
            }else {
                homeTeam.getTeamStats().setSetRatio( (float)homeTeam.getTeamStats().getSetsWon() / homeTeam.getTeamStats().getSetsLost());
            }

            //Setting sets ratio for away team
            if (awayTeam.getTeamStats().getSetsLost() == 0) {
                awayTeam.getTeamStats().setSetRatio(awayTeam.getTeamStats().getSetsWon());
            }else {
                awayTeam.getTeamStats().setSetRatio( (float)awayTeam.getTeamStats().getSetsWon() / awayTeam.getTeamStats().getSetsLost());
            }

            //Setting points scored by both teams
            //Tutaj seria ifów żeby rozwiącać problem pustych pól jeżeli mecz jest 3 setowy
            homeTeam.getTeamStats().setPointsWon(homeTeam.getTeamStats().getPointsWon() + Integer.parseInt(match.getMatchResult().getHomeTeamSet1Score()) + Integer.parseInt(match.getMatchResult().getHomeTeamSet2Score()) + Integer.parseInt(match.getMatchResult().getHomeTeamSet3Score()) + Integer.parseInt(match.getMatchResult().getHomeTeamSet4Score()) + Integer.parseInt(match.getMatchResult().getHomeTeamSet5Score()));
            homeTeam.getTeamStats().setPointsLost(homeTeam.getTeamStats().getPointsLost() + Integer.parseInt(match.getMatchResult().getAwayTeamSet1Score()) + Integer.parseInt(match.getMatchResult().getAwayTeamSet2Score()) + Integer.parseInt(match.getMatchResult().getAwayTeamSet3Score()) + Integer.parseInt(match.getMatchResult().getAwayTeamSet4Score()) + Integer.parseInt(match.getMatchResult().getAwayTeamSet5Score()));
            homeTeam.getTeamStats().setTeamPointsRatio((float) homeTeam.getTeamStats().getPointsWon() / homeTeam.getTeamStats().getPointsLost());
            awayTeam.getTeamStats().setPointsWon(awayTeam.getTeamStats().getPointsWon() + Integer.parseInt(match.getMatchResult().getAwayTeamSet1Score()) + Integer.parseInt(match.getMatchResult().getAwayTeamSet2Score()) + Integer.parseInt(match.getMatchResult().getAwayTeamSet3Score()) + Integer.parseInt(match.getMatchResult().getAwayTeamSet4Score()) + Integer.parseInt(match.getMatchResult().getAwayTeamSet5Score()));
            awayTeam.getTeamStats().setPointsLost(awayTeam.getTeamStats().getPointsLost() + Integer.parseInt(match.getMatchResult().getHomeTeamSet1Score()) + Integer.parseInt(match.getMatchResult().getHomeTeamSet2Score()) + Integer.parseInt(match.getMatchResult().getHomeTeamSet3Score()) + Integer.parseInt(match.getMatchResult().getHomeTeamSet4Score()) + Integer.parseInt(match.getMatchResult().getHomeTeamSet5Score()));
            awayTeam.getTeamStats().setTeamPointsRatio((float) awayTeam.getTeamStats().getPointsWon() / awayTeam.getTeamStats().getPointsLost());


            teamService.addTeam(homeTeam);
            teamService.addTeam(awayTeam);
        }
    }
}

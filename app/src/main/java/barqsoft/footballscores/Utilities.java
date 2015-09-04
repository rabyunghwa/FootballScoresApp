package barqsoft.footballscores;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities {

    public static final int BUNDESLIGA1 = 394;
    public static final int BUNDESLIGA2 = 395;
    public static final int LIGUE1 = 396;
    public static final int LIGUE2 = 397;
    public static final int PREMIER_LEAGUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int SEGUNDA_DIVISION = 400;
    public static final int SERIE_A = 401;
    public static final int PRIMEIRA_LIGA = 402;
    public static final int BUNDESLIGA3 = 403;
    public static final int EREDIVISIE = 404;
    public static final int CHAMPIONS_LEAGUE = 405;

    public static String getLeague(int league_num) {
        switch (league_num) {
            case SERIE_A : return "Seria A";
            case PREMIER_LEAGUE : return "Premier League";
            case BUNDESLIGA1 : return "Bundesliga 1";
            case BUNDESLIGA2 : return "Bundesliga 2";
            case BUNDESLIGA3 : return "Bundesliga 3";
            case PRIMERA_DIVISION : return "Primera Division";
            case LIGUE1 : return "Ligue 1";
            case LIGUE2 : return "Ligue 2";
            case SEGUNDA_DIVISION : return "Segunda Division";
            case PRIMEIRA_LIGA : return "Primeira Liga";
            case EREDIVISIE : return "Eredivisie";
            case CHAMPIONS_LEAGUE : return "Champions League";
            default: return "Not known League Please report";
        }
    }
    public static String getMatchDay(int match_day,int league_num)
    {
        if(league_num == PREMIER_LEAGUE) {
            if (match_day <= 6) {
                return "Group Stages, Matchday : 6";
            } else if(match_day == 7 || match_day == 8) {
                return "First Knockout round";
            } else if(match_day == 9 || match_day == 10) {
                return "QuarterFinal";
            } else if(match_day == 11 || match_day == 12) {
                return "SemiFinal";
            } else {
                return "Final";
            }
        }
        else {
            return "Matchday : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals) {
        if(home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname) {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname) {
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "FC Bayern München" : return R.drawable.fc_bayern;
            case "FC Augsburg" : return R.drawable.fc_augsburg;
            case "Hamburger SV" : return R.drawable.hamburger_sv;
            case "Bayer Leverkusen" : return R.drawable.bayer_leverkusen;
            case "Hertha BSC" : return R.drawable.hertha_bsc;
            case "TSG 1899 Hoffenheim" : return R.drawable.tsg_1899;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            case "Ajax Amsterdam" : return R.drawable.ajax_amsterdam;
            case "ADO Den Haag" : return R.drawable.ado_hen_haag;
            case "1. FC Nürnberg" : return R.drawable.fc_nrnberg;
            case "Fortuna Düsseldorf" : return R.drawable.fortuna_dsseldorf;
            case "Eintracht Braunschweig" : return R.drawable.eintracht_braunschweig;
            case "Karlsruher SC" : return R.drawable.ksc;
            case "FSV Frankfurt" : return R.drawable.fsv_frankfurt;
            case "FC St. Pauli" : return R.drawable.fc_st_pauli;
            case "AS Saint-Étienne" : return R.drawable.as_saint_tienne;
            case "SC Bastia" : return R.drawable.sc_bastia;
            case "Mainz 05 II" : return R.drawable.mainz_05;
            case "SG Sonnenhof Großaspach" : return R.drawable.sg_sonnenhof_groaspach;
            case "VfR Aalen" : return R.drawable.vfr_aalen;
            case "FC Hansa Rostock" : return R.drawable.fc_hansa;
            case "Norwich City FC" : return R.drawable.norwich_city_fc;
            default: return R.drawable.no_icon;
        }
    }
}

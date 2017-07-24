package marmot.leaguemastery;

/**
 * Created by marmot on 7/21/2017.
 */

public class Summoner {
    // Database fields

    private int summonerTableId;
    private int listId;

    // Champion Mastery Information Fields

    private String profileIconId;
    private String name;
    private String summonerLevel;
    private String revisionDate;
    private String summonerId;
    private String accountId;

    // Other constants

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String ZERO = "0";

    public Summoner() {
        this.profileIconId = ZERO;
        this.name = "";
        this.summonerLevel = ZERO;
        this.revisionDate = ZERO;
        this.summonerId = ZERO;
        this.accountId = ZERO;
    }

    public Summoner(int listId,
                    String profileIconId,
                    String name,
                    String summonerLevel,
                    String revisionDate,
                    String summonerId,
                    String accountId) {
        this.listId = listId;
        this.profileIconId = profileIconId;
        this.name = name;
        this.summonerLevel = summonerLevel;
        this.revisionDate = revisionDate;
        this.summonerId = summonerId;
        this.accountId = accountId;
    }

    public Summoner(int summonerTableId,
                    int listId,
                    String profileIconId,
                    String name,
                    String summonerLevel,
                    String revisionDate,
                    String summonerId,
                    String accountId) {
        this.summonerTableId = summonerTableId;
        this.listId = listId;
        this.profileIconId = profileIconId;
        this.name = name;
        this.summonerLevel = summonerLevel;
        this.revisionDate = revisionDate;
        this.summonerId = summonerId;
        this.accountId = accountId;
    }

    public int getSummonerTableId() {
        return summonerTableId;
    }

    public void setSummonerTableId(int summonerTableId) {
        this.summonerTableId = summonerTableId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(String profileIconId) {
        this.profileIconId = profileIconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(String summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public String getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(String revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}

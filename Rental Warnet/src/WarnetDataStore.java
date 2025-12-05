import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WarnetDataStore {

    public static GameApp[] getGames() {
        java.util.List<GameApp> list = new java.util.ArrayList<>();

        String sql = """
            SELECT game_id, title, genre, year, developer, description, image_path, imdb_url
            FROM game
            ORDER BY game_id ASC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String title   = rs.getString("title");
                String genre   = rs.getString("genre");
                String year    = rs.getString("year");
                String dev     = rs.getString("developer");
                String desc    = rs.getString("description");
                String imgPath = rs.getString("image_path");
                String url     = rs.getString("imdb_url");

                list.add(new GameApp(title, genre, year, dev, desc, imgPath, url));
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil daftar game dari DB: " + e.getMessage());
            e.printStackTrace();
        }

        return list.toArray(new GameApp[0]);
    }

    private static final int PC_COUNT = 12;

    private static final boolean[] pcBusy = new boolean[PC_COUNT + 1];
    private static final long[] pcEndTime = new long[PC_COUNT + 1];

    public static int getPcCount() {
        return PC_COUNT;
    }

    public static synchronized void startPcSession(int stationNumber, long durationMillis) {
        if (stationNumber < 1 || stationNumber > PC_COUNT) {
            return;
        }
        long now = System.currentTimeMillis();
        pcBusy[stationNumber] = true;
        pcEndTime[stationNumber] = now + durationMillis;
        System.out.println("Start session Station " + stationNumber
                + " dur=" + durationMillis + " endAt=" + pcEndTime[stationNumber]);
    }

    public static synchronized boolean isPcBusy(int stationNumber) {
        if (stationNumber < 1 || stationNumber > PC_COUNT) {
            return false;
        }
        if (!pcBusy[stationNumber]) {
            return false;
        }

        long now = System.currentTimeMillis();
        if (now >= pcEndTime[stationNumber]) {
            pcBusy[stationNumber] = false;
            pcEndTime[stationNumber] = 0L;
            return false;
        }
        return true;
    }

    public static synchronized long getRemainingMillisForPc(int stationNumber) {
        if (!isPcBusy(stationNumber)) {
            return 0L;
        }
        long remaining = pcEndTime[stationNumber] - System.currentTimeMillis();
        return Math.max(0L, remaining);
    }

    private static final Map<String, Integer> userActivePc = new HashMap<>();
    private static final Map<String, Long> userEndTime = new HashMap<>();

    public static synchronized void startUserPcSession(String username, int stationNumber, long durationMillis) {
        if (username == null || username.isEmpty()) {
            return;
        }
        if (stationNumber < 1 || stationNumber > PC_COUNT) {
            return;
        }

        long now = System.currentTimeMillis();
        long end = now + durationMillis;

        pcBusy[stationNumber] = true;
        pcEndTime[stationNumber] = end;

        userActivePc.put(username, stationNumber);
        userEndTime.put(username, end);

        System.out.println("User " + username + " start on Station " + stationNumber
                + " dur=" + durationMillis + " endAt=" + end);
    }

    public static synchronized boolean userHasActiveSession(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        Integer station = userActivePc.get(username);
        Long end = userEndTime.get(username);

        if (station == null || end == null) {
            return false;
        }

        long now = System.currentTimeMillis();
        if (now >= end) {
            userActivePc.remove(username);
            userEndTime.remove(username);
            return false;
        }

        return true;
    }

    public static synchronized long getUserRemainingMillis(String username) {
        if (!userHasActiveSession(username)) {
            return 0L;
        }
        long end = userEndTime.get(username);
        long remaining = end - System.currentTimeMillis();
        return Math.max(0L, remaining);
    }

    public static synchronized int getUserActiveStation(String username) {
        if (!userHasActiveSession(username)) {
            return -1;
        }
        Integer station = userActivePc.get(username);
        return station == null ? -1 : station;
    }

    public static synchronized void clearUserSession(String username) {
        Integer station = userActivePc.remove(username);
        userEndTime.remove(username);

        if (station != null && station >= 1 && station <= PC_COUNT) {
            pcBusy[station] = false;
            pcEndTime[station] = 0L;
        }
    }

    public static synchronized void syncFromDatabase() {
        java.util.List<DatabaseConnection.ActiveSession> sessions =
                DatabaseConnection.getActiveSessionsFromDb();
        long now = System.currentTimeMillis();

        java.util.Iterator<Map.Entry<String, Long>> uit = userEndTime.entrySet().iterator();
        while (uit.hasNext()) {
            Map.Entry<String, Long> e = uit.next();
            if (now >= e.getValue()) {
                String user = e.getKey();
                Integer station = userActivePc.remove(user);
                uit.remove();
                if (station != null && station >= 1 && station <= PC_COUNT) {
                    pcBusy[station] = false;
                    pcEndTime[station] = 0L;
                }
            }
        }

        for (DatabaseConnection.ActiveSession s : sessions) {
            if (s.userId == null) {
                continue;
            }
            String username = DatabaseConnection.getUsernameByUserId(s.userId);
            if (username == null) {
                continue;
            }
            if (s.endMillis > now) {
                userActivePc.put(username, s.pcId);
                userEndTime.put(username, s.endMillis);
            } else {
                if (s.pcId >= 1 && s.pcId <= PC_COUNT) {
                    pcBusy[s.pcId] = false;
                    pcEndTime[s.pcId] = 0L;
                }
            }
        }
    }
}
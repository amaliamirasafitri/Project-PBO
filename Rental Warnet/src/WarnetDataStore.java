
import java.util.HashMap;
import java.util.Map;

public class WarnetDataStore {

    public static GameApp[] getGames() {
        return new GameApp[]{
            new GameApp(
            "Counter-Strike 2",
            "FPS, Shooter, Multiplayer, Competitive, Action, eSports",
            "2012",
            "Valve",
            "For over two decades, Counter-Strike has offered an elite competitive experience, one shaped by millions of players from across the globe. And now the next chapter in the CS story is about to begin. This is Counter-Strike 2.\r\n"
            + "\r\n"
            + "A free upgrade to CS:GO, Counter-Strike 2 marks the largest technical leap in Counter-Strike’s history. Built on the Source 2 engine, Counter-Strike 2 is modernized with realistic physically-based rendering, state of the art networking, and upgraded Community Workshop tools.",
            "Assets/Counter Strike 2.png",
            "https://store.steampowered.com/app/730/CounterStrike_2/"
            ),
            new GameApp(
            "Dota 2",
            "MOBA, Multiplayer, Strategy, eSports",
            "2013",
            "Valve",
            "Every day, millions of players worldwide enter battle as one of over a hundred Dota heroes. And no matter if it's their 10th hour of play or 1,000th, there's always something new to discover. With regular updates that ensure a constant evolution of gameplay, features, and heroes, Dota 2 has taken on a life of its own.",
            "Assets/Dota 2.png",
            "https://store.steampowered.com/app/570/Dota_2/"
            ),
            new GameApp(
            "Marvel Rivals",
            "Multiplayer, Hero Shooter, PvP, Superhero",
            "2024",
            "NetEase Games",
            "Marvel Rivals is a Super Hero Team-Based PVP Shooter! Assemble an all-star Marvel squad, devise countless strategies by combining powers to form unique Team-Up skills and fight in destructible, ever-changing battlefields across the continually evolving Marvel universe!",
            "Assets/Marvel Rivals.png",
            "https://store.steampowered.com/app/2767030/Marvel_Rivals/"
            ),
            new GameApp(
            "Apex Legends",
            "Multiplayer, Battle Royale, Shooter, FPS",
            "2020",
            "Respawn",
            "Apex Legends is the award-winning, free-to-play Hero Shooter from Respawn Entertainment. Master an ever-growing roster of legendary characters with powerful abilities, and experience strategic squad play and innovative gameplay in the next evolution of Hero Shooter and Battle Royale.",
            "Assets/Apex Legends.png",
            "https://store.steampowered.com/app/1172470/Apex_Legends/"
            ),
            new GameApp(
            "Walker",
            "Horror, Co-op, Survival",
            "2026",
            "Dadilip",
            "An online co-op horror game where you and your crew hunt monsters inside a massive WALKER. Use your environment, items, or anything at your disposal to protect your WALKER.",
            "Assets/Walker.png",
            "https://store.steampowered.com/app/3484210/WALKER/"
            ),
            new GameApp(
            "R.E.P.O",
            "Horror, Co-op, Multiplayer, Comedy",
            "2025",
            "semiwork",
            "An online co-op horror game with up to 6 players. Locate valuable, fully physics-based objects and handle them with care as you retrieve and extract to satisfy your creator's desires.",
            "Assets/R.E.P.O.png",
            "https://store.steampowered.com/app/3241660/REPO/"
            ),
            new GameApp(
            "PEAK",
            "Multiplayer, Co-op, Physics",
            "2025",
            "Team PEAK",
            "PEAK is a co-op climbing game where the slightest mistake can spell your doom. Either solo or as a group of lost nature scouts, your only hope of rescue from a mysterious island is to scale the mountain at its center. Do you have what it takes to reach the PEAK?",
            "Assets/PEAK.png",
            "https://store.steampowered.com/app/3527290/PEAK/"
            ),
            new GameApp(
            "Where Winds Meet",
            "Open World, Multiplayer, CO-OP",
            "2025",
            "Everstone Studio",
            "Where Winds Meet is an epic Wuxia open-world action-adventure RPG set in ancient China at the tenth century. Players will assume the role of a young sword master as they embark on a journey to uncover the mysteries of their own identity. Let the wind carry your legend on an epic journey to the East!",
            "Assets/Where Winds Meet.png",
            "https://store.steampowered.com/app/3564740/Where_Winds_Meet/"
            ),
            new GameApp(
            "Wuthering Waves",
            "Open World, Anime, Action",
            "2025",
            "KURO GAMES",
            "Wuthering Waves is a story-rich open-world action RPG with a high degree of freedom. You wake from your slumber as Rover, joined by a vibrant cast of Resonators on a journey to reclaim your lost memories and change the world.",
            "Assets/Wuthering Waves.png",
            "https://store.steampowered.com/app/3513350/Wuthering_Waves/"
            )
        };
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

    /**
     * Sinkronkan status PC (in-use dan waktu selesai) dari database. Metode ini
     * mengambil transaksi dengan status 'pending' dan mengisi array internal
     * `pcBusy` dan `pcEndTime` sehingga status tetap konsisten setelah restart
     * aplikasi atau setelah user logout/login kembali.
     */
    public static synchronized void syncFromDatabase() {
        java.util.List<DatabaseConnection.ActiveSession> sessions = DatabaseConnection.getActiveSessionsFromDb();
        long now = System.currentTimeMillis();

        // Clear any expired in-memory user sessions
        java.util.Iterator<java.util.Map.Entry<String, Long>> uit = userEndTime.entrySet().iterator();
        while (uit.hasNext()) {
            java.util.Map.Entry<String, Long> e = uit.next();
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

        // Populate per-user active sessions from DB. We will not mark global pcBusy
        // for other users; only when that user logs in we will reflect their session.
        for (DatabaseConnection.ActiveSession s : sessions) {
            if (s.userId == null) {
                continue;
            }
            String username = DatabaseConnection.getUsernameByUserId(s.userId);
            if (username == null) {
                continue;
            }
            if (s.endMillis > now) {
                // register user session in-memory so when that user logs in it will be visible
                userActivePc.put(username, s.pcId);
                userEndTime.put(username, s.endMillis);
                // Do not set pcBusy globally here; keep visibility per-user
            } else {
                // expired
                // ensure cleared
                if (s.pcId >= 1 && s.pcId <= PC_COUNT) {
                    pcBusy[s.pcId] = false;
                    pcEndTime[s.pcId] = 0L;
                }
            }
        }
    }
}

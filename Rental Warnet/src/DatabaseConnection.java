import java.sql.*;

public class DatabaseConnection {

    private static final String DB_NAME = "db_warnet";
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String URL = BASE_URL + DB_NAME;
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        initDatabase();
    }

    private static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(BASE_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            System.err.println("Gagal membuat database: " + e.getMessage());
        }

        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL DEFAULT 'operator'
            )
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createUsersTable);
        } catch (SQLException e) {
            System.err.println("Gagal membuat tabel users: " + e.getMessage());
        }

        String createPaketTable = """
            CREATE TABLE IF NOT EXISTS paket (
                paket_id INT AUTO_INCREMENT PRIMARY KEY,
                nama_paket VARCHAR(100) NOT NULL,
                deskripsi_singkat TEXT NOT NULL,
                fitur TEXT NOT NULL,
                durasi INT NOT NULL,
                harga INT NOT NULL
            )
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createPaketTable);

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM paket");
            int count = 0;
            if (rs.next()) {
                count = rs.getInt("cnt");
            }

            if (count == 0) {
                String insert = """
                    INSERT INTO paket (nama_paket, deskripsi_singkat, fitur, durasi, harga) VALUES
                    ('Paket Bronze (1 Jam)',
                     'Cocok buat kamu yang cuma ingin main sebentar atau sekadar mengisi waktu luang.',
                     '• 1 jam bermain\\n• Bebas pilih game\\n• Kontrol PC\\n• Headset & sofa nyaman',
                     1, 12000),
                    ('Paket Silver (3 Jam)',
                     'Pilihan pas untuk sesi bermain lebih lama tanpa khawatir waktu cepat habis.',
                     '• 3 jam bermain\\n• Game kapan saja\\n• Konsol & mesin ringan\\n• WIFI gratis & ruangan ber-AC',
                     3, 30000),
                    ('Paket Gold (5 Jam)',
                     'Buat kamu yang mau puas main game favorit tanpa batas waktu yang mepet!',
                     '• 5 jam bermain\\n• Akses semua game premium\\n• 1 minuman gratis\\n• Prioritas room',
                     5, 50000),
                    ('Paket Malam',
                     'Spesial untuk gamer malam yang mau suasana tenang dan bebas ngetes.',
                     '• Bermain nonstop jam 00:00-06:00\\n• Snack & minuman gratis\\n• Ruang nyaman dengan lampu redup\\n• Diskon untuk member',
                     6, 60000)
                    """;

                stmt.executeUpdate(insert);
            }

        } catch (SQLException e) {
            System.err.println("Gagal membuat/isi tabel paket: " + e.getMessage());
        }

        String createPcTable = """
            CREATE TABLE IF NOT EXISTS pc (
                pc_id INT AUTO_INCREMENT PRIMARY KEY,
                nama_pc VARCHAR(100) NOT NULL,
                status VARCHAR(20) NOT NULL DEFAULT 'available'
            )
        """;

        String createGameTable = """
            CREATE TABLE IF NOT EXISTS game (
                game_id INT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                genre VARCHAR(255) NOT NULL,
                year VARCHAR(10) NOT NULL,
                developer VARCHAR(255) NOT NULL,
                description TEXT NOT NULL,
                image_path VARCHAR(512) NOT NULL,
                imdb_url VARCHAR(512) NOT NULL
            )
        """;

        String createTransaksiTable = """
            CREATE TABLE IF NOT EXISTS transaksi (
                id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
                id_user INT,
                id_pc INT NOT NULL,
                id_paket INT NOT NULL,
                kode_acak VARCHAR(100),
                qr_kode VARCHAR(255),
                waktu_mulai DATETIME,
                waktu_selesai DATETIME,
                durasi_ms BIGINT,
                total_harga INT,
                status VARCHAR(30) DEFAULT 'pending',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE SET NULL ON UPDATE CASCADE,
                FOREIGN KEY (id_pc) REFERENCES pc(pc_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                FOREIGN KEY (id_paket) REFERENCES paket(paket_id) ON DELETE RESTRICT ON UPDATE CASCADE
            )
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createPcTable);
            stmt.executeUpdate(createGameTable);
            stmt.executeUpdate(createTransaksiTable);
        } catch (SQLException e) {
            System.err.println("Gagal membuat tabel PC/game/transaksi: " + e.getMessage());
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM pc");
            int cnt = 0;
            if (rs.next()) {
                cnt = rs.getInt("cnt");
            }
            if (cnt == 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("INSERT INTO pc (nama_pc, status) VALUES ");
                for (int i = 1; i <= 12; i++) {
                    sb.append("('PC " + i + "', 'available')");
                    if (i < 12) {
                        sb.append(",");
                    }
                }
                stmt.executeUpdate(sb.toString());
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengisi tabel PC contoh: " + e.getMessage());
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM game");
            int cnt = 0;
            if (rs.next()) {
                cnt = rs.getInt("cnt");
            }

            if (cnt == 0) {
                String insertGames = """
                    INSERT INTO game (title, genre, year, developer, description, image_path, imdb_url) VALUES
                    ('Counter-Strike 2',
                     'FPS, Shooter, Multiplayer, Competitive, Action, eSports',
                     '2012',
                     'Valve',
                     'Game FPS kompetitif 5v5 dengan gameplay cepat dan fokus ke aim dan strategi tim.',
                     'Assets/Counter Strike 2.png',
                     'https://store.steampowered.com/app/730/CounterStrike_2/'),
                    ('Dota 2',
                     'MOBA, Multiplayer, Strategy, eSports',
                     '2013',
                     'Valve',
                     'Game MOBA 5v5 dengan ratusan hero dan gameplay yang dalam.',
                     'Assets/Dota 2.png',
                     'https://store.steampowered.com/app/570/Dota_2/'),
                    ('Marvel Rivals',
                     'Multiplayer, Hero Shooter, PvP, Superhero',
                     '2024',
                     'NetEase Games',
                     'Hero shooter PvP dengan karakter-karakter dari Marvel Universe.',
                     'Assets/Marvel Rivals.png',
                     'https://store.steampowered.com/app/2767030/Marvel_Rivals/'),
                    ('Apex Legends',
                     'Multiplayer, Battle Royale, Shooter, FPS',
                     '2020',
                     'Respawn',
                     'Battle royale hero shooter dengan kemampuan unik tiap legend dan movement cepat.',
                     'Assets/Apex Legends.png',
                     'https://store.steampowered.com/app/1172470/Apex_Legends/'),
                    ('Walker',
                     'Horror, Co-op, Survival',
                     '2026',
                     'Dadilip',
                     'Game horror co-op di mana pemain menjelajah walker raksasa dan menghindari monster.',
                     'Assets/Walker.png',
                     'https://store.steampowered.com/app/3484210/WALKER/'),
                    ('R.E.P.O',
                     'Horror, Co-op, Multiplayer, Comedy',
                     '2025',
                     'semiwork',
                     'Game horror co-op dengan nuansa komedi, fokus mengambil barang bernilai tinggi.',
                     'Assets/R.E.P.O.png',
                     'https://store.steampowered.com/app/3241660/REPO/'),
                    ('PEAK',
                     'Multiplayer, Co-op, Physics',
                     '2025',
                     'Team PEAK',
                     'Game co-op panjat tebing dengan kontrol fisik yang susah tapi seru.',
                     'Assets/PEAK.png',
                     'https://store.steampowered.com/app/3527290/PEAK/'),
                    ('Where Winds Meet',
                     'Open World, Multiplayer, CO-OP',
                     '2025',
                     'Everstone Studio',
                     'RPG open world bertema wuxia di Tiongkok kuno.',
                     'Assets/Where Winds Meet.png',
                     'https://store.steampowered.com/app/3564740/Where_Winds_Meet/'),
                    ('Wuthering Waves',
                     'Open World, Anime, Action',
                     '2025',
                     'KURO GAMES',
                     'Action RPG open world bergaya anime dengan sistem combat yang dinamis.',
                     'Assets/Wuthering Waves.png',
                     'https://store.steampowered.com/app/3513350/Wuthering_Waves/')
                    """;

                stmt.executeUpdate(insertGames);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengisi tabel game: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static java.util.List<PaketData> getAllPaket() {
        java.util.List<PaketData> list = new java.util.ArrayList<>();
        String sql = "SELECT paket_id, nama_paket, deskripsi_singkat, fitur, durasi, harga FROM paket";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("paket_id");
                String nama = rs.getString("nama_paket");
                String deskripsi = rs.getString("deskripsi_singkat");
                String fitur = rs.getString("fitur");
                int durasi = rs.getInt("durasi");
                int harga = rs.getInt("harga");

                String hargaFormatted = "Rp" + String.format("%,d", harga).replace(",", ".");

                list.add(new PaketData(id, nama, deskripsi, fitur, hargaFormatted, durasi));
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil paket: " + e.getMessage());
        }

        return list;
    }

    public static class PaketData {
        public final int paketId;
        public final String namaPaket;
        public final String deskripsiSingkat;
        public final String fitur;
        public final String harga;
        public final int durasi;

        public PaketData(int paketId, String namaPaket, String deskripsiSingkat,
                         String fitur, String harga, int durasi) {
            this.paketId = paketId;
            this.namaPaket = namaPaket;
            this.deskripsiSingkat = deskripsiSingkat;
            this.fitur = fitur;
            this.harga = harga;
            this.durasi = durasi;
        }
    }

    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'operator')";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Username sudah terpakai!");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateLogin(String inputUser, String inputPass) {
        boolean isValid = false;
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inputUser);
            stmt.setString(2, inputPass);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isValid = true;
            }

        } catch (SQLException e) {
            System.err.println("Koneksi Gagal: " + e.getMessage());
        }
        return isValid;
    }

    public static int createTransaksi(Integer userId, int pcId, int paketId,
                                      String kodeAcak, String qrKodenya,
                                      long durasiMs, int totalHarga) {
        String sql = """
            INSERT INTO transaksi
            (id_user, id_pc, id_paket, kode_acak, qr_kode, durasi_ms, total_harga, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, 'pending')
        """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (userId != null) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setInt(2, pcId);
            ps.setInt(3, paketId);
            ps.setString(4, kodeAcak);
            ps.setString(5, qrKodenya);
            ps.setLong(6, durasiMs);
            ps.setInt(7, totalHarga);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                return -1;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal membuat transaksi: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public static class ActiveSession {
        public final Integer userId;
        public final int pcId;
        public final long endMillis;

        public ActiveSession(Integer userId, int pcId, long endMillis) {
            this.userId = userId;
            this.pcId = pcId;
            this.endMillis = endMillis;
        }
    }

    public static java.util.List<ActiveSession> getActiveSessionsFromDb() {
        java.util.List<ActiveSession> out = new java.util.ArrayList<>();
        String sql = "SELECT id_user, id_pc, created_at, durasi_ms FROM transaksi WHERE status = 'pending'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Integer userId = null;
                int uid = rs.getInt("id_user");
                if (!rs.wasNull()) {
                    userId = uid;
                }
                int pcId = rs.getInt("id_pc");
                Timestamp ts = rs.getTimestamp("created_at");
                long dur = rs.getLong("durasi_ms");
                long created = ts == null ? System.currentTimeMillis() : ts.getTime();
                long end = created + dur;
                out.add(new ActiveSession(userId, pcId, end));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil active sessions dari DB: " + e.getMessage());
            e.printStackTrace();
        }
        return out;
    }

    public static Integer getUserIdByUsername(String username) {
        if (username == null) {
            return null;
        }
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    return id;
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mencari user id: " + e.getMessage());
        }
        return null;
    }

    public static String getUsernameByUserId(int userId) {
        String sql = "SELECT username FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mencari username: " + e.getMessage());
        }
        return null;
    }

    public static boolean updatePcStatus(int pcId, String status) {
        String sql = "UPDATE pc SET status = ? WHERE pc_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, pcId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal update status PC: " + e.getMessage());
            return false;
        }
    }
}
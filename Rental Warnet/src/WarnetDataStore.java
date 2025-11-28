public class WarnetDataStore {

    public static BillingPackage[] getPaketList() {
        // TODO: isi sesuai paket billingmu
        return new BillingPackage[0];
    }

    public static GameApp[] getGames() {
        return new GameApp[] {
            new GameApp(
                "Counter-Strike 2",
                "FPS, Shooter, Multiplayer, Competitive, Action, eSports",
                "2012",
                "Valve",
                "For over two decades, Counter-Strike has offered an elite competitive experience, one shaped by millions of players from across the globe. And now the next chapter in the CS story is about to begin. This is Counter-Strike 2.\r\n" + //
                                        "\r\n" + //
                                        "A free upgrade to CS:GO, Counter-Strike 2 marks the largest technical leap in Counter-Strike’s history. Built on the Source 2 engine, Counter-Strike 2 is modernized with realistic physically-based rendering, state of the art networking, and upgraded Community Workshop tools.",
                "Assets/Counter Strike 2.png",
                "https://store.steampowered.com/app/730/CounterStrike_2/"
            ),
            new GameApp(
                "The Last of Us Part I",
                "Action, Drama",
                "2022",
                "Naughty Dog",
                "Perjalanan Joel dan Ellie melintasi Amerika pasca-kiamat penuh infeksi mematikan dan manusia yang putus asa.",
                "/assets/games/tlou1.png",
                "https://www.imdb.com/title/tt2140553/"
            ),
            new GameApp(
                "God of War Ragnarök",
                "Action",
                "2022",
                "Santa Monica Studio",
                "Kratos dan Atreus menghadapi takdir dan ancaman Ragnarök di dunia mitologi Nordik.",
                "/assets/games/gow_ragnarok.png",
                "https://www.imdb.com/title/tt11700534/"
            ),
            new GameApp(
                "Elden Ring",
                "Action RPG",
                "2022",
                "FromSoftware",
                "Petualangan di The Lands Between untuk menjadi Elden Lord dengan dunia terbuka luas dan tingkat kesulitan tinggi.",
                "/assets/games/elden_ring.png",
                "https://www.imdb.com/title/tt9184986/"
            )
        };
    }
}

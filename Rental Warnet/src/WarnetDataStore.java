public class WarnetDataStore {

    public static BillingPackage[] getPaketList() {
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
            )
        };
    }
}

# 🛡️ Vulcan AntiCheat - Deobfuscated & Clean Source Code

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.8--1.20.4-blue.svg)](https://spigotmc.org/)
[![Build](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()
[![Code Quality](https://img.shields.io/badge/Code-100%25%20Human--Readable-success.svg)]()

Fully deobfuscated, cleaned, and 100% human-readable source code of **Vulcan AntiCheat** version **2.9.7.23**.
Structured as a standard Maven project (src/main/java), completely free of decompilation artifacts, compiling standalone with zero errors.

---

## 🔍 Reverse Engineering & Deobfuscation Analysis

The original Vulcan-2.9.7.23.jar was protected by multiple layers of **Zelix KlassMaster (ZKM)** commercial obfuscation. Below is a breakdown of the protection mechanisms and how they were resolved:

### 1. InvokeDynamic (Indy) & String Decryption
* **Original state:** All string literals (messages, check names, config keys, network packets) were hidden behind invokedynamic instructions pointing to Bootstrap Methods (BSM) with byte-level XOR keys.
* **Resolution:** All string constants were extracted, decrypted, and restored as clean Java string literals. Bootstrap tables and decryptor classes were stripped.

### 2. Control Flow Flattening
* **Original state:** Check handlers and internal state trackers (StatusTracker, BlockBreakTracker, ReachEntityHelper) were mangled into complex switch-case state machines with dummy branching and opaque predicates.
* **Resolution:** Algorithms were reconstructed into natural if-else condition trees, structured loops, and clean early returns.

### 3. Metadata & Identifier Reconstruction
* **Original state:** Classes, fields, methods, and parameters were renamed to single-letter or synthetic identifiers ( c, SyntheticClass_1).
* **Resolution:** Full semantic reconstruction:
  - 181 check classes mapped into logical packages: combat, movement, player.
  - Math and physics engines restored: BoundingBoxUtil, TrigUtil, CollisionUtil, Ray3d.
  - Violations, tick counters, buffers, and deltas given clear names (deltaXZ, 	icksSinceAttac, isStandingOnScaffold,..).

### 4. DRM & Licensing Bypass
* **Original state:** Embedded SpigotMC User ID, Resource ID, Nonce validation and bytecode payload decryption via AES/MD5 stack tracing.
* **Resolution:** External licensing callbacks were detached; the plugin is completely self-contained and ready to run on any standalone server.

---

## 📦 Project Structure

`
AcV2/
├── src/
│   ├── main/
│   │   ├── java/                # 533 clean Java source files
│   │   │   ├── me/frep/vulcan/  # Core anticheat, trackers, GUI, checks
│   │   │   │   └── check/impl/  # 181 check implementations (combat, movement, player)
│   │   │   ├── ac/vulcan/       # Scheduler bridges (Folia / Paper / Bukkit)
│   │   │   └── org/apache/commons/ # Apache Commons Lang 3 utilities
│   │   └── resources/           # plugin.yml, config.yml, checks.txt, stats.yml
├── tools/                       # Stubs generator and build.py script
├── pom.xml                      # Standard Maven POM
└── README.md                    # Project documentation
`

---

## ⚡ How to Build

### Quick Standalone Build (Python)
`ash
python tools/build.py
`
*Compiles the entire tree with javac and packages the clean JAR into build/Vulcan-2.9.7.23-deobf.jar.*

### Maven Build
`bash
mvn clean package
`

---

## 📋 Requirements & Dependencies

| Component | Requirement |
|---|---|
| **Java** | Java 8, 11, 16, 17, 21+ |
| **Server Software** | Spigot, Paper, Purpur, Pufferfish, Folia (1.8.8 – 1.20.4+) |
| **Required Plugin** | [PacketEvents 2.x](https://github.com/retrooper/packetevents) |
| **Optional Plugins** | ProtocolLib, PlaceholderAPI, Vault, DiscordSRV |

---

## 🛡️ Checks Breakdown (181 Checks)

<details>
<summary><b>⚔️ Combat Checks (62)</b> — Click to expand</summary>

- **Aim:** AimA – AimY
- **AutoClicker:** AutoClickerA – AutoClickerT
- **AutoBlock:** AutoBlockA – AutoBlockD
- **KillAura / Reach:** KillAuraA – KillAuraL, ReachA – ReachB, HitboxA – HitboxB
- **Velocity & Misc:** VelocityA – VelocityD, CriticalsA – CriticalsB, FastBowA
</details>

<details>
<summary><b>🏃 Movement Checks (52)</b> — Click to expand</summary>

- **Flight & Speed:** FlightA – FlightF, SpeedA – SpeedE, SprintA, StrafeA
- **Physics & Water:** JesusA – JesusE, JumpA – JumpD, MotionA – MotionE, FastClimbA, WallClimbA, StepA, StepC
- **Vehicles & Elytra:** BoatFlyA – BoatFlyC, EntitySpeedA, EntityFlightA – EntityFlightB, NoSaddleA, ElytraA – ElytraN, AntiLevitationA, VClipA, NoSlowA – NoSlowC
</details>

<details>
<summary><b>📦 Player & World Checks (67)</b> — Click to expand</summary>

- **BadPackets:** BadPackets5 – BadPackets9, BadPacketsA – BadPacketsZ
- **Scaffold & Build:** ScaffoldA – ScaffoldN, TowerA, FastPlaceA, FastBreakA, AirPlaceA, GhostHandA
- **Spoof & Automation:** BaritoneA – BaritoneB, GroundSpoofA – GroundSpoofC, ImprobableA – ImprobableF, InvalidA – InvalidJ, TimerA, TimerD
</details>

---

## ⚖️ Disclaimer
This repository is published strictly for educational and research purposes: studying anticheat architectures, analyzing Java bytecode obfuscation, and researching Minecraft network protocols.

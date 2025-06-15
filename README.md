# Pac-Man (Java Swing Edition)

A lightweight, **pure-Java** re-creation of the classic 1980 arcade game.  
Built with nothing more than the Java SE platform and the Swing GUI toolkit, this project is perfect for:

* **Teaching** intro-level game-loop architecture, basic AI, and event-driven graphics
* **Demonstrating** Breadth-First Search (BFS) path-finding in a grid world
* **Having fun** and showing off a fully working desktop game that compiles in seconds

---
# 🎮 game_shots

<table>
  <tr>
    <td align="center">
      <h4>Pac-Man Game Loop</h4>
      <img src="GAMELOOP.png" width="300" alt="Pac-Man Game Loop">
    </td>
    <td align="center">
      <h4>Game Over Sequence</h4>
      <img src="GAMEOVER.png" width="300" alt="Game Over Sequence">
    </td>
  </tr>
</table>

---

## ✨ Features

| Category | Highlights |
|----------|------------|
| Gameplay | • Original maze & pellets<br/>• Four ghosts with individual sprites<br/>• Score, lives, win/lose logic |
| Graphics | • Tile-based board (32 × 32 px)<br/>• Double-buffered Swing painting<br/>• Custom sprite sheet (`*.png`) loading |
| AI       | • **BFS** every frame for smart ghost pursuit<br/>• Simple speed differential (Pac-Man is 2× faster than ghosts) |
| Code     | • Fewer than 600 source lines<br/>• No external libraries<br/>• Clear separation of model (Block), view (paintComponent), and controller (KeyListener) |

---

## 🕹️ Controls

| Key | Action |
|-----|--------|
| ⬆  | Move up |
| ⬇  | Move down |
| ⬅  | Move left |
| ➡  | Move right |
| *Any arrow after GAME OVER* | Restart with full lives |

---

## 📂 Project Layout

<?php
// Recursively render a nested <ul> tree of any directory
function renderTree(string $dir) {
    echo "<ul>\n";
    foreach (scandir($dir) as $item) {
        if ($item === '.' || $item === '..') continue;
        $path = "$dir/$item";
        if (is_dir($path)) {
            echo "<li class=\"folder\">📁 $item\n";
            renderTree($path);
            echo "</li>\n";
        } else {
            $ext = strtolower(pathinfo($item, PATHINFO_EXTENSION));
            if (in_array($ext, ['png','jpg','jpeg','gif'])) {
                // Image file: thumbnail + name
                echo <<<HTML
<li class="file image">
  <div class="thumb"><img src="$path" alt="$item"></div>
  <span class="filename">$item</span>
</li>

HTML;
            } else {
                // Regular file
                echo "<li class=\"file\">📄 $item</li>\n";
            }
        }
    }
    echo "</ul>\n";
}
?><!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>📂 Project Layout</title>
  <style>
    body { margin:0; font-family:sans-serif; display:flex; height:100vh; }
    .tree { flex:0 0 300px; overflow:auto; border-right:1px solid #ddd; padding:1em; }
    .tree ul { list-style:none; padding-left:1em; }
    .tree li { margin:0.2em 0; }
    .folder { font-weight:bold; cursor:pointer; }
    .file { margin-left:1.2em; }
    .thumb img { max-width:60px; border:1px solid #ccc; display:block; margin-bottom:0.2em; }
    .gallery { flex:1; padding:1em; display:grid;
               grid-template-columns:repeat(auto-fill,minmax(100px,1fr));
               gap:1em; overflow:auto; }
    .gallery figure { text-align:center; font-size:0.85em; }
    .gallery img { max-width:100%; border:1px solid #ccc; }
  </style>
</head>
<body>
  <!-- Left: file tree -->
  <nav class="tree">
    <?php renderTree('src'); ?>
    <ul><li class="file">📄 README.md</li></ul>
  </nav>

  <!-- Right: auto‐picked PNG gallery -->
  <section class="gallery">
    <?php
      foreach (glob('src/resources/*.png') as $img) {
          $name = basename($img);
          echo "<figure>\n";
          echo "  <img src=\"$img\" alt=\"$name\">\n";
          echo "  <figcaption>$name</figcaption>\n";
          echo "</figure>\n";
      }
    ?>
  </section>
</body>
</html>


---

## 🚀 Quick Start

1. **Prerequisites**

    * Java 11 or newer (JDK) on your `PATH`

2. **Compile**

   ```bash
   cd src
   javac Main.java PacMan.java

### In an IDE

1. Create a standard **Java SE** project.
2. Drop `src/` into the source folder and mark `resources/` as a resource root.
3. Run `Main`.

---

### 🔍 How It Works

- **Tile map** – `String[]` where
    - `X` = wall
    - (space) = pellet
    - `P` = Pac-Man spawn
    - `b / o / r / p` = ghosts
- **Game loop** – `Timer(55 ms)` → `move()` → `repaint()` ≈ 18 FPS
- **Movement** – Pac-Man speed = `tileSize / 4`, ghosts = `tileSize / 8`
- **Path-finding** – Each ghost runs **BFS** on a passable grid every frame
- **Collision** – Axis-aligned bounding boxes for walls, pellets & ghosts

---

### 🛠️ Customization

| Constant                   | File                         | Purpose            |
|----------------------------|------------------------------|--------------------|
| `rowCount`, `colCount`, `tileSize` | `Main.java` & `PacMan.java` | Board dimensions   |
| `tileMap[]`                | `PacMan.java`                | Level design       |
| `Timer(55, …)`             | `PacMan()`                   | Frame delay (ms)   |

---

### 🗺️ Roadmap

- Power-pellets & frightened mode
- High-score persistence
- Sound effects & music
- Multiple levels / difficulty scaling
- Android port (Java FX / LibGDX)

---

### 🤝 Contributing

1. **Fork** → create a feature branch
2. Commit with descriptive messages
3. Open a **pull request** with screenshots/GIFs

---

### 📜 License

Released under the **MIT License** – see `LICENSE`.

---

### 🙏 Credits

- Original game © 1980 **NAMCO**
- BFS reference – Amit Patel, *Red Blob Games*
- Sprites – [Kenney.nl](https://kenney.nl)

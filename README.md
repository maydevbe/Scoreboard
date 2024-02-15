# Scoreboard
Este plugin de Scoreboard fue diseñado para maximizar la eficiencia y proporcionar la mejor experiencia posible a tus jugadores. ¡Esperamos que lo disfrutes al máximo!

## Características
* Puedes agregar hasta 42 caracteres por línea.
* Personaliza fácilmente el diseño y la información mostrada en la scoreboard.

## Cómo usar
###### Para implementar el plugin en tu servidor, simplemente sigue estos pasos:
###### Configura el scoreboard según tus preferencias utilizando el siguiente código:
```java
public class TestScoreboardLayout extends AbstractScoreboardLayout {

    @Override
    public ScoreboardLayoutManager createLayout(Player player) {
        ScoreboardLayoutManager layoutManager = new ScoreboardLayoutManager();
        layoutManager.setTitle("&b&lScoreboard API");

        layoutManager.addLine(" ");
        layoutManager.addLine("&bPlayers&7: &f" + Bukkit.getOnlinePlayers().size());
        layoutManager.addLine("&bRandom Number&7: &f" + ThreadLocalRandom.current().nextInt(1000));
        layoutManager.addLine(" ");

        return layoutManager;
    }
}
```
###### ¡Y eso es todo! Ahora puedes disfrutar de un scoreboard de jugadores personalizado en tu servidor de Minecraft.

## Próxima actualización
En la próxima versión del plugin, se agregará soporte para más versiones de Minecraft, lo que garantizará una mayor compatibilidad con diferentes configuraciones de servidores.

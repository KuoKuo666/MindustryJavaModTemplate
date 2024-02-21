package statecraft;

import arc.*;
import arc.graphics.Color;
import arc.util.*;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.content.UnitTypes;
import mindustry.game.EventType.*;
import mindustry.game.Team;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.maps.planet.MarSaraPlanetGenerator;
import mindustry.mod.*;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Planet;
import mindustry.ui.dialogs.*;
import mindustry.world.Block;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;

public class StarCraftMain extends Mod{
    public Planet marsara;

    // 声明矿物
    public Item jtk, gnws;

    // 声明重构的核心基地等建筑
    public Block baseCenter;

    public StarCraftMain(){
        Log.info("Loaded StarCraftMain constructor.");

        //listen for game load event
        Events.on(ClientLoadEvent.class, e -> {
            //show dialog upon startup
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                dialog.cont.add("behold").row();
                //mod sprites are prefixed with the mod name (this mod is called 'example-java-mod' in its config)
                dialog.cont.image(Core.atlas.find("rts-star-craft-mod-frog")).pad(20f).row();
                dialog.cont.button("I see", dialog::hide).size(100f, 50f);
                dialog.show();
            });
        });
    }

    @Override
    public void loadContent(){


        jtk = new Item("jtk", Color.valueOf("00bbf1")){{
            hardness = 5;
            alwaysUnlocked = true;
        }};
        gnws = new Item("gnws", Color.valueOf("43ea00")){{
            hardness = 6;
            alwaysUnlocked = true;
        }};
        baseCenter = new CoreBlock("ren-order-center"){{
            requirements(Category.effect, BuildVisibility.editorOnly, ItemStack.with(jtk, 400));
            alwaysUnlocked = true;

            isFirstTier = true;
            unitType = UnitTypes.alpha;
            health = 1500;
            itemCapacity = 20000;
            size = 4;
            unitCapModifier = 8;
        }};

        marsara = new Planet("marsara", Planets.sun, 1.3f, 2){{
            localizedName = Core.bundle.get("planet.marsara");
            generator = new MarSaraPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 5);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("eba768").a(0.75f), 2, 0.42f, 1f, 0.43f),
                    new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("eea293").a(0.75f), 2, 0.42f, 1.2f, 0.45f)
            );
            alwaysUnlocked = true;
            landCloudColor = Color.valueOf("ed6542");
            atmosphereColor = Color.valueOf("f07218");
            defaultEnv = Env.terrestrial;
            startSector = 20;
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            tidalLock = true;
            orbitSpacing = 2f;
            lightSrcTo = 0.5f;
            lightDstFrom = 0.2f;
            clearSectorOnLose = true;
            defaultCore = baseCenter;
            iconColor = Color.valueOf("ff9266");
            hiddenItems.addAll(Items.serpuloItems).addAll(Items.erekirItems);
            allowLaunchToNumbered = false;
            updateLighting = false;

            ruleSetter = r -> {
                r.waveTeam = Team.malis;
                r.placeRangeCheck = false;
                r.showSpawns = true;
                r.fog = true;
                r.staticFog = true;
                r.lighting = false;
                r.coreDestroyClear = true;
                r.onlyDepositCore = true;
            };
        }};
    }

}

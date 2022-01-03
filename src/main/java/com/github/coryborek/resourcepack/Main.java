package com.github.coryborek.resourcepack;

import com.agentdid127.resourcepack.forwards.ForwardsPackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.pack.ZipPack;
import org.apache.commons.io.FileUtils;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    /**
     * This program converts a 1.12 pack to multiple new versions, as opposed to running the ResourcePackConverter multiple times.
     * @param args inputted arguments
     */
    public static void main(String[] args) throws IOException {
        //Welcome Users, notify of original software
        System.out.println("Welcome to the ResourcePack Multiversion Converter");
        System.out.println("This uses the agentdid127 Resource Pack Converter. https://github.com/agentdid127/ResourcePackConverter");
        System.out.println("");

        //Gathers arguments needed for conversion
        System.out.println("Gathering arguments...");
        String from = "1.12";
        String light = "front";
        boolean minify = true;

        String packn = args[0];

        //Notify Users of pack being converted.
        System.out.println("Pack: " + args[0]);
        PrintStream out = System.out;

        //These are the pack and converted pack.
        Path currentPack = Paths.get("./" + packn);
        String extension = "";
        if (!currentPack.toFile().isDirectory()) extension = ".zip";
        Path convertedPack = updateConverted(currentPack, extension);


        System.out.println("Beginning Conversions");
        if (!currentPack.toFile().exists()) return;

        Pack pack = updatePack(currentPack);



        updateVersion("1.12", "1.13", out, getFinishedPath(convertedPack, "null", "1.13", extension), pack, convertedPack);
        currentPack = getFinishedPath(convertedPack, "null","1.13", extension);
        pack = updatePack(currentPack);
        convertedPack = updateConverted(currentPack, extension);

        updateVersion("1.13", "1.15", out, getFinishedPath(convertedPack, "1.13", "1.15", extension), pack, convertedPack);
        currentPack = getFinishedPath(convertedPack, "1.13","1.15", extension);
        pack = updatePack(currentPack);
        convertedPack = updateConverted(currentPack, extension);

        updateVersion("1.15", "1.16.2", out, getFinishedPath(convertedPack, "1.15","1.16.2", extension), pack, convertedPack);
        currentPack = getFinishedPath(convertedPack, "1.15","1.16.2", extension);
        pack = updatePack(currentPack);
        convertedPack = updateConverted(currentPack, extension);

        updateVersion("1.16.2", "1.18", out, getFinishedPath(convertedPack, "1.16.2", "1.18", extension), pack, convertedPack);

        System.out.println("Thank you for using the converter!");
    }

    public static void updateVersion(String from, String to, PrintStream out, Path finishedPath, Pack pack, Path convertedPack) throws IOException {
        System.out.println("Converting to " + to);
        new ForwardsPackConverter(from, to, "front", true, Paths.get("./"), true, out).runPack(pack);
        Util.copyDir(convertedPack, finishedPath);
        FileUtils.forceDelete(convertedPack.toFile());
    }

    public static Path getFinishedPath (Path pack, String from, String to, String extension) {
        return Paths.get("./" + pack.toFile().getName().replace("_" + from.replace(".", "_"), "").replace("_converted", "").replace(".zip", "") + "_" + to.replace(".", "_") + extension);
    }
    
    
    public static Pack updatePack(Path currentPack) {
        Pack pack;
        if (currentPack.toFile().getName().endsWith(".zip")) pack = new ZipPack(currentPack);
        else pack = new Pack(currentPack);
        
        return pack;
    }

    public static Path updateConverted(Path current, String extension) {
        return current.getParent().resolve(current.toFile().getName().replace(".zip", "") + "_converted" + extension);
    }
}

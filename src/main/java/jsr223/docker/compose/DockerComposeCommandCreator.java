package jsr223.docker.compose;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created on 4/23/2015.
 */
public class DockerComposeCommandCreator {

    // Constants
    private static String yamlFileName = "docker-compose.yml";
    private static String bashCreateYamlFileCommandStart = "echo \"";
    private static String bashCreateYamlFileCommandEnd = "\" > "+yamlFileName;
    private static String lineSeparator = "\n";
    private static String filenameArgument = "-f";
    private static String setupContainerArgument = "up";


    /**
     * Creates and returns a command which creates a file to be read by the docker-compose client.
     * @param yamlFile
     * @return
     */
    public static String[] createDockerComposeFileCreationCommand(@NotNull String yamlFile) {
        ArrayList<String> command = new ArrayList<>();
        command.add(bashCreateYamlFileCommandStart);
        command.addAll(Arrays.asList(escapeArrayOfStringsForBash(yamlFile)));
        command.add(bashCreateYamlFileCommandEnd);
        return command.toArray(new String[command.size()]);
    }


    /**
     * This method creates a bash command which starts docker-compose with a given yaml file.
     * @return A String array which contains the command as a separate @String and each
     * argument as a separate String.
     */
    public static String[] createDockerComposeExecutionCommandBash() {
        List<String> command = new ArrayList<>();

        // Add sudo if necessary
        if (DockerComposeScriptEngineFactory.isUseSudo()) {
            command.add(DockerComposeScriptEngineFactory.getSudoCommand());
        }

        // Add docker compose command
        command.add(DockerComposeScriptEngineFactory.getDockerComposeCommand());

        // Add filename argument
        command.add(filenameArgument);

        // Add filename
        command.add(yamlFileName);

        // Start container with argument
        command.add(setupContainerArgument);

        return command.toArray(new String[command.size()]);
    }

    /**
     * This methods creates an escape map. All future escapes necessary to run a compose script properly
     * will be added.
     * @return A map which contains a key (string to be replaced) and a value (replaced).
     */
    private static Map<String,String> getEscapeMap() {
        Map<String, String> replaceThat = new HashMap<>();

        // Add everything which should be escaped and how
        // Replace " with \"
        replaceThat.put("\"", "\\\"");

        return replaceThat;
    }

    /**
     * Escape an array of @Strings.
     * @param stringsToEscape Array where each string will be escaped.
     * @return Array which contains all
     */
    private static String[] escapeArrayOfStringsForBash(@NotNull String... stringsToEscape) {
        ArrayList<String> result = new ArrayList<>();
        Map<String, String> replaceThat = getEscapeMap();

        for (String line : stringsToEscape) {
            result.add(replaceAllOccurrences(line, replaceThat));
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * Replaces all occurrences in a String given via a Map<StringToReplace,Replaced>.
     * @param string String which will have replaced all occurrences.
     * @param replaceMap Map<StringToReplace,Replaced>.
     * @return String where all occurrences are replaced.
     */
    private static String replaceAllOccurrences(@NotNull String string, @NotNull Map<String, String> replaceMap) {
        for (String replaceEntry : replaceMap.keySet()) {
            string = string.replace(replaceEntry, replaceMap.get(replaceEntry));
        }
        return string;
    }
}
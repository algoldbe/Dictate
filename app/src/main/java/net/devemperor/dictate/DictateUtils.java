package net.devemperor.dictate;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictateUtils {

    public static final String PROMPT_PUNCTUATION_CAPITALIZATION = "The Great Wall of China, the Eiffel Tower, the Pyramids of Giza, and the Statue of Liberty are among the most iconic landmarks in the world, and they draw countless tourists every year who marvel at their grandeur and historical significance.";
    public static final String PROMPT_REWORDING_BE_PRECISE = "Be accurate with your output. Only output exactly what the user has asked for above. Do not add any text before or after the actual output. Output the text in the language of the instruction, unless a different language was explicitly requested.";

    public static double calcModelCost(String modelName, long audioTime, long inputTokens, long outputTokens) {
        switch (modelName) {
            case "whisper-1":  // whisper-1 and gpt-4o-transcribe cost the same
            case "gpt-4o-transcribe":
                return audioTime * 0.0001f;
            case "gpt-4o-mini-transcribe":
                return audioTime * 0.00005f;
            case "whisper-large-v3-turbo":
            case "whisper-large-v3":
                return audioTime * 0.0001f;  // Assuming same cost as whisper-1

            case "o3-mini":  // o3-mini and o1-mini cost the same
            case "o1-mini":
                return inputTokens * 0.0000011f + outputTokens * 0.0000044f;
            case "o1":
                return inputTokens * 0.000015f + outputTokens * 0.00006f;
            case "gpt-4o-mini":
                return inputTokens * 0.00000015f + outputTokens * 0.0000006f;
            case "gpt-4o":
                return inputTokens * 0.0000025f + outputTokens * 0.00001f;
            case "gpt-4-turbo":
                return inputTokens * 0.00001f + outputTokens * 0.00003f;
            case "gpt-4":
                return inputTokens * 0.00003f + outputTokens * 0.00006f;
            case "gpt-3.5-turbo":
                return inputTokens * 0.0000005f + outputTokens * 0.0000015f;
            default:
                return 0;
        }
    }

    public static String translateModelName(String modelName) {
        switch (modelName) {
            case "whisper-1":
                return "Whisper V2";
            case "gpt-4o-transcribe":
                return "GPT-4o transcribe";
            case "gpt-4o-mini-transcribe":
                return "GPT-4o mini transcribe";
            case "o3-mini":
                return "OpenAI o3 mini";
            case "o1-mini":
                return "OpenAI o1 mini";
            case "o1":
                return "OpenAI o1";
            case "gpt-4o-mini":
                return "GPT-4o mini";
            case "gpt-4o":
                return "GPT-4o";
            case "gpt-4-turbo":
                return "GPT-4 Turbo";
            case "gpt-4":
                return "GPT-4";
            case "gpt-3.5-turbo":
                return "GPT-3.5 Turbo";
            case "whisper-large-v3-turbo":
                return "Whisper Large V3 Turbo";
            case "whisper-large-v3":
                return "Whisper Large V3";
            case "llama-3.3-70b-versatile":
                return "Llama 3.3 70B Versatile";
            default:
                return "Unknown";
        }
    }

    public static long getAudioDuration(File file) {
        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {
            retriever.setDataSource(file.getAbsolutePath());
            String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            retriever.release();
            if (durationStr != null) {
                return Long.parseLong(durationStr) / 1000; // duration in seconds
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean isValidProxy(String proxy) {
        if (proxy == null) return false;

        if (proxy.startsWith("http://") || proxy.startsWith("https://")) {
            return false;
        }

        // Check if the proxy is in the format "IP:Port" or "Domain:Port"
        String regex = "^(?:" +
                // Group 1: IP address (four octets separated by dots)
                "((?:\\d{1,3}\\.){3}\\d{1,3})" +
                "|" +
                // Group 2: Domain name (at least two parts separated by dots)
                "((?:[a-zA-Z0-9][-a-zA-Z0-9]*\\.)+[a-zA-Z]{2,})" +
                ")" +
                // Group 3: Port number
                ":(\\d+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(proxy);
        if (!matcher.matches()) {
            return false;
        }

        // Check if port is between 1 and 65535
        int port;
        try {
            port = Integer.parseInt(Objects.requireNonNull(matcher.group(3)));
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        if (port < 1 || port > 65535) {
            return false;
        }

        // Check if each part of the IP address is between 0 and 255
        String ipPart = matcher.group(1);
        if (ipPart != null) {
            String[] octets = ipPart.split("\\.");
            for (String octet : octets) {
                int value;
                try {
                    value = Integer.parseInt(octet);
                } catch (NumberFormatException e) {
                    return false;
                }
                if (value < 0 || value > 255) {
                    return false;
                }
            }
        }

        return true;
    }


    public static String translateLanguageToEmoji(String language) {
        switch (language) {
            case "detect":
                return "✨";
            case "af":
                return "\uD83C\uDDFF\uD83C\uDDE6";
            case "ar":
                return "\uD83C\uDDF8\uD83C\uDDE6";
            case "hy":
                return "\uD83C\uDDE6\uD83C\uDDF2";
            case "az":
                return "\uD83C\uDDE6\uD83C\uDDFF";
            case "be":
                return "\uD83C\uDDE7\uD83C\uDDFE";
            case "bs":
                return "\uD83C\uDDE7\uD83C\uDDE6";
            case "bg":
                return "\uD83C\uDDE7\uD83C\uDDEC";
            case "ca":
                return "\uD83C\uDDE6\uD83C\uDDE9";
            case "zh":
                return "\uD83C\uDDE8\uD83C\uDDF3";
            case "hr":
                return "\uD83C\uDDED\uD83C\uDDF7";
            case "cs":
                return "\uD83C\uDDE8\uD83C\uDDFF";
            case "da":
                return "\uD83C\uDDE9\uD83C\uDDF0";
            case "nl":
                return "\uD83C\uDDF3\uD83C\uDDF1";
            case "en":
                return "\uD83C\uDDEC\uD83C\uDDE7";
            case "et":
                return "\uD83C\uDDEA\uD83C\uDDEA";
            case "fi":
                return "\uD83C\uDDEB\uD83C\uDDEE";
            case "fr":
                return "\uD83C\uDDEB\uD83C\uDDF7";
            case "gl":
                return "\uD83C\uDDEA\uD83C\uDDF8";
            case "de":
                return "\uD83C\uDDE9\uD83C\uDDEA";
            case "el":
                return "\uD83C\uDDEC\uD83C\uDDF7";
            case "he":
                return "\uD83C\uDDEE\uD83C\uDDF1";
            case "hi":
                return "\uD83C\uDDEE\uD83C\uDDF3";
            case "hu":
                return "\uD83C\uDDED\uD83C\uDDFA";
            case "is":
                return "\uD83C\uDDEE\uD83C\uDDF8";
            case "id":
                return "\uD83C\uDDEE\uD83C\uDDE9";
            case "it":
                return "\uD83C\uDDEE\uD83C\uDDF9";
            case "ja":
                return "\uD83C\uDDEF\uD83C\uDDF5";
            case "kn":
                return "\uD83C\uDDE8\uD83C\uDDE6";
            case "kk":
                return "\uD83C\uDDF0\uD83C\uDDFF";
            case "ko":
                return "\uD83C\uDDF0\uD83C\uDDF7";
            case "lv":
                return "\uD83C\uDDF1\uD83C\uDDFB";
            case "lt":
                return "\uD83C\uDDF1\uD83C\uDDF9";
            case "mk":
                return "\uD83C\uDDF2\uD83C\uDDF0";
            case "ms":
                return "\uD83C\uDDF2\uD83C\uDDFE";
            case "mr":
                return "\uD83C\uDDEE\uD83C\uDDF3";
            case "mi":
                return "\uD83C\uDDF3\uD83C\uDDFF";
            case "ne":
                return "\uD83C\uDDF3\uD83C\uDDF5";
            case "no":
                return "\uD83C\uDDF3\uD83C\uDDF4";
            case "fa":
                return "\uD83C\uDDEE\uD83C\uDDF7";
            case "pl":
                return "\uD83C\uDDF5\uD83C\uDDF1";
            case "pt":
                return "\uD83C\uDDF5\uD83C\uDDF9";
            case "ro":
                return "\uD83C\uDDF7\uD83C\uDDF4";
            case "ru":
                return "\uD83C\uDDF7\uD83C\uDDFA";
            case "sr":
                return "\uD83C\uDDF7\uD83C\uDDF8";
            case "sk":
                return "\uD83C\uDDF8\uD83C\uDDF0";
            case "sl":
                return "\uD83C\uDDF8\uD83C\uDDEE";
            case "es":
                return "\uD83C\uDDEA\uD83C\uDDF8";
            case "sw":
                return "\uD83C\uDDF9\uD83C\uDDFF";
            case "sv":
                return "\uD83C\uDDF8\uD83C\uDDEA";
            case "tl":
                return "\uD83C\uDDF5\uD83C\uDDED";
            case "ta":
                return "\uD83C\uDDF1\uD83C\uDDF0";
            case "th":
                return "\uD83C\uDDF9\uD83C\uDDED";
            case "tr":
                return "\uD83C\uDDF9\uD83C\uDDF7";
            case "uk":
                return "\uD83C\uDDFA\uD83C\uDDE6";
            case "ur":
                return "\uD83C\uDDF5\uD83C\uDDF0";
            case "vi":
                return "\uD83C\uDDFB\uD83C\uDDF3";
            case "cy":
                return "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F";
            default:
                return "";
        }
    }
}

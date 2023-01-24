package generation;

import lombok.SneakyThrows;
import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jsonschema2Pojo;
import generation.rules.CustomRuleFactory;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class GenerateModels {
    @SneakyThrows
    public static void main(String[] args) {
        Iterator<URL> source = Stream.of(System.getProperty("source")).filter(Objects::nonNull)
                .map(s -> s.split(","))
                .flatMap(Arrays::stream)
                .map(GenerateModels::fileUrl)
                .toList()
                .iterator();
        GenerationConfig config = new DefaultGenerationConfig() {
            @SneakyThrows
            @Override
            public Iterator<URL> getSource() {
                return source;
            }

            @Override
            public String getTargetPackage() {
                return System.getProperty("targetPackage");
            }

            @Override
            public File getTargetDirectory() {
                return new File(System.getProperty("targetDirectory"));
            }

            @Override
            public boolean isIncludeToString() {
                return false;
            }

            @Override
            public boolean isInitializeCollections() {
                return false;
            }

            @Override
            public Class<? extends RuleFactory> getCustomRuleFactory() {
                return CustomRuleFactory.class;
            }
        };

        try (var dirStream = Files.walk(Paths.get(config.getTargetDirectory().getPath(), config.getTargetPackage().split("\\.")))) {
            dirStream
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder())
                    .forEach(File::delete);
        }

        Jsonschema2Pojo.generate(config, new Slf4jRuleLogger());
    }

    @SneakyThrows
    private static URL fileUrl(String p) {
        return Path.of(p).toUri().toURL();
    }
}

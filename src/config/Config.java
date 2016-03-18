package config;

import java.util.List;

public interface Config {
	abstract List<ScrapeParam> readParams();
}

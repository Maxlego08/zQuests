package fr.maxlego08.quests.zcore.utils.plugins;

public enum Plugins {
	
	VAULT("Vault"),
	ESSENTIALS("Essentials"),
	HEADDATABASE("HeadDatabase"), 
	PLACEHOLDER("PlaceholderAPI"),
	CITIZENS("Citizens"),
	TRANSLATIONAPI("TranslationAPI"),
	ZTRANSLATOR("zTranslator"),
	BLOCKTRACKER("BlockTracker"),
	WILDSTACKER("WildStacker"),
	ZJOBS("zJobs"),
	SUPERIORSKYBLOCK2("SuperiorSkyblock2"),
	ZSHOP("zShop"),

	;

	private final String name;

	private Plugins(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}

package com.stats.lolgg.model;

public enum ChampEnum {
    // 사이온까지
    ATROX("Aatrox", ""),
    AHRI("Ahri", ""),
    AKALI("Akali", ""),
    AKSHAN("Akshan", ""),
    ALISTAR("Alistar", ""),
    APHELIOS("Aphelios", ""),
    ASHE("Ashe", ""),
    AURELION_SOL("Aurelion Sol", ""),
    AZIR("Azir", ""),
    BARD("Bard", "바드"),
    BELVETH("Belveth", "벨베스"),
    BLITZCRANK("Blitzcrank", "블리츠크랭크"),
    BRAND("Brand", "브랜드"),
    BRAUM("Braum", "브라움"),
    BRIAR("Briar", "브라이어"),
    CAITLYN("Caitlyn", ""),
    CAMILLE("Camille", ""),
    CASSIOPEIA("Cassiopeia", ""),
    CHO_GATH("Cho'Gath", ""),
    CORKI("Corki", ""),
    DARIUS("Darius", "다리우스"),
    DIANA("Diana", "다이애나"),
    DRAVEN("Draven", "드레이븐"),
    //문도박사
    ELISE("Elise", ""),
    EVELYNN("Evelynn", ""),
    EZREAL("Ezreal", ""),
    GALIO("Galio", "갈리오"),
    GAREN("Garen", "가렌"),
    GNAR("Gnar", "나르"),
    GRAGAS("Gragas", "그라가스"),
    GRAVES("Graves", ""),
    GANGPLANK("Gangplank","갱플랭크"),
    GWEN("Gwen", "그웬"),
    HECARIM("Hecarim", ""),
    HWEI("Hwei", ""),
    ILLAOI("Illaoi", ""),
    IRELIA("Irelia", ""),
    JARVAN_IV("Jarvan IV", ""),
    JAX("Jax", ""),
    JAYCE("Jayce", ""),
    JHIN("Jhin", ""),
    JINX("Jinx", ""),
    KAI_SA("Kai'Sa", ""),
    KALISTA("Kalista", ""),
    KARMA("Karma", ""),
    KARTHUS("Karthus", ""),
    KATARINA("Katarina", ""),
    KAYLE("Kayle", ""),
    KAYN("Kayn", ""),
    KENNEN("Kennen", ""),
    KHA_ZIX("Kha'Zix", ""),
    KSANTE("KSante", ""),
    LEBLANC("LeBlanc", "르블랑"),
    LEE_SIN("Lee Sin", "리 신"),
    LEONA("Leona", "레오나"),
    LILLIA("Lillia", "릴리아"),
    LISSANDRA("Lissandra", "리산드라"),
    LUCIAN("Lucian", "루시안"),
    LULU("Lulu", "룰루"),
    LUX("Lux", "럭스"),
    MALPHITE("Malphite", "말파이트"),
    MAOKAI("Maokai", "마오카이"),
    MISS_FORTUNE("Miss Fortune", "미스 포츈"),
    MONKEY_KING("Monkey King", ""),
    MORDEKAISER("Mordekaiser", "모데카이저"),
    MORGANA("Morgana", "모르가나"),
    MASTER_YI("Master Yi","마스터 이"),
    MALZAHAR("Malzahar","말자하"),
    MILIO("Milio","밀리오"),
    NAMI("Nami", "나미"),
    NASUS("Nasus", "나서스"),
    NAAFIRI("Naafiri","나피리"),
    NAUTILUS("Nautilus", "노틸러스"),
    NEEKO("Neeko", "니코"),
    NIDALEE("Nidalee", "니달리"),
    NOCTURNE("Nocturne", "녹턴"),
    //누누
    NILAH("Nilah","닐라"),
    OLAF("Olaf", ""),
    ORIANNA("Orianna", ""),
    PANTHEON("Pantheon", ""),
    POPPY("Poppy", "뽀삐"),
    PYKE("Pyke", ""),
    RAKAN("Rakan", "라칸"),
    RAMMUS("Rammus", "람머스"),
    REK_SAI("Rek'Sai", "렉사이"),
    RELL("Rell", "렐"),
    RENEKTON("Renekton", "레넥톤"),
    RENGAR("Rengar", "렝가"),
    RUMBLE("Rumble", "럼블"),
    RYZE("Ryze", "라이즈"),
    RENATA_GLASC("Renata Glasc","레나타 글라스크"),
    RIVEN("Riven","리븐"),
    SAMIRA("Samira", "사미라"),
    SEJUANI("Sejuani", ""),
    SENNA("Senna", ""),
    SERAPHINE("Seraphine", ""),
    SHACO("Shaco", ""),
    SHEN("Shen", ""),
    SINGED("Singed", ""),
    SIVIR("Sivir", ""),
    SKARNER("Skarner", ""),
    SMOLDER("Smolder", ""),
    SORAKA("Soraka", ""),
    SWAIN("Swain", ""),
    SYLAS("Sylas", ""),
    TALIYAH("Taliyah", ""),
    TEEMO("Teemo", ""),
    THRESH("Thresh", ""),
    TRISTANA("Tristana", ""),
    TRUNDLE("Trundle", ""),
    TRYNDAMERE("Tryndamere", ""),
    TWISTED_FATE("Twisted Fate", ""),
    TWITCH("Twitch", ""),
    VARUS("Varus", "바루스"),
    VAYNE("Vayne", "베인"),
    VEL_KOZ("Vel'Koz", "벨코즈"),
    VEX("Vex", "벡스"),
    VI("Vi", "바이"),
    VIEGO("Viego", "비에고"),
    VIKTOR("Viktor", "빅토르"),
    VOLIBEAR("Volibear", "볼리베어"),
    VEIGAR("Veigar","베이가"),
    VLADIMIR("Vladimir","블라디미르"),
    WARWICK("Warwick", ""),
    XAYAH("Xayah", ""),
    XERATH("Xerath", ""),
    XIN_ZHAO("Xin Zhao", ""),
    YASUO("Yasuo", ""),
    YONE("Yone", ""),
    YORICK("Yorick", ""),
    YUUMI("Yuumi", "");

    private String englishChampName;
    private String koreanChampName;

    ChampEnum(String englishChampName,String koreanChampName){
        this.englishChampName = englishChampName;
        this.koreanChampName = koreanChampName;
    }

    public String getEnglishChampName() {
        return englishChampName;
    }

    public String getKoreanChampName() {
        return koreanChampName;
    }

    public static String getKoreanValue(String englishValue) {
        for (ChampEnum mappingEnum : ChampEnum.values()) {
            if (mappingEnum.getEnglishChampName().equals(englishValue)) {
                return mappingEnum.getKoreanChampName();
            }
        }
        return null; // 매핑되는 값이 없을 경우
    }


    
}

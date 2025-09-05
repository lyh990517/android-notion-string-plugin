# 🌍 Notion Stringboard Plugin

A powerful Gradle plugin that automatically generates Android string resources from your Notion database with advanced filtering and multi-language support.

## ✨ Features

- 🌐 **Multi-language support**: Generate string resources for 35+ languages
- 🔄 **Automated sync**: Fetch translations directly from Notion database
- 📱 **Android optimized**: Generates proper Android XML string resources
- 🏗️ **Build integration**: Seamlessly integrates with your Gradle build process
- 🎯 **Advanced filtering**: Use complex queries to filter your Notion data
- 📊 **Sorting options**: Sort by properties, timestamps, and custom criteria

## 🚀 Quick Setup

### 1️⃣ Apply the plugin

In your `build.gradle.kts` (Module level):

```kotlin
plugins {
    id("io.github.lyh990517.notion-stringboard") version "1.0.2"
}
```

### 2️⃣ Configure the plugin

```kotlin
stringboard {
    // Required: Your Notion credentials
    notionApiKey = "your_notion_integration_token"
    dataSourceId = "your_notion_datasource_id"
    
    // Output directory for generated resources
    outputDir = "${project.rootDir}/app/src/main/res"
    
    // Define your supported languages
    languages = listOf(
        Language.English("String: BASE"),
        Language.Korean("String: KOR"), 
        Language.Japanese("String: JPN")
    )
    
    // Optional: Advanced filtering and sorting
    queryBuilder = NotionQueryBuilder()
        .filter {
            richText { "String: BASE" contains "hello" } and
            select { "Status" equals "Published" }
        }
        .sort {
            property { "Resource ID" by Direction.ASCENDING }
        }
}
```

### 3️⃣ Set up Your Notion Database

Create a Notion database with these columns:
> 📋 **Reference**: [Sample database template](https://wealthy-client-873.notion.site/25b8bc2ec9148051ac4beb6b9aaf914a?v=25b8bc2ec91480a299ff000c8da4da11&source=copy_link)

| Column Name  | Type      | Purpose                      |
|--------------|-----------|------------------------------|
| Resource ID  | Title     | Android string resource name |
| String: BASE | Rich Text | English/default text         |
| String: KOR  | Rich Text | Korean translation           |
| String: JPN  | Rich Text | Japanese translation         |
| Status       | Select    | Publication status           |

**Example data:**
| Resource ID | String: BASE | String: KOR | String: JPN | Status |
|-------------|--------------|-------------|-------------|---------|
| hello_world | Hello World! | 안녕하세요! | こんにちは！ | Published |
| welcome_msg | Welcome | 환영합니다 | ようこそ | Published |

<details>
<summary>📸 <b>Database Screenshot</b></summary>

<img width="1463" height="342" alt="스크린샷 2025-09-05 오후 1 28 43" src="https://github.com/user-attachments/assets/db3e09f9-7b34-407f-b626-50b01c011fc6" />

</details>

---

## ⚙️ Configuration Options

<details>
<summary><b>📋 Basic Configuration</b></summary>

```kotlin
stringboard {
    notionApiKey = "your_token"           // Required: Notion API key
    dataSourceId = "database_id"          // Required: Database ID
    outputDir = "src/main/res"            // Optional: Output directory
    idPropertyName = "Resource ID"        // Optional: ID column name
}
```

</details>

<details>
<summary><b>🌐 Language Support</b></summary>

The plugin supports **35+ languages**. Add any combination:

```kotlin
languages = listOf(
    // Asian Languages
    Language.Korean("String: KOR"),
    Language.Japanese("String: JPN"), 
    Language.ChiSimplified("String: CHS"),
    Language.ChiTraditional("String: CHT"),
    Language.Thai("String: THA"),
    Language.Vietnamese("String: VIE"),
    
    // European Languages  
    Language.English("String: BASE"),
    Language.Spanish("String: SPA"),
    Language.French("String: FRA"),
    Language.German("String: DEU"),
    Language.Italian("String: ITA"),
    Language.Portuguese("String: POR"),
    Language.Dutch("String: NLD"),
    Language.Russian("String: RUS"),
    
    // And many more...
)
```

</details>

<details>
<summary><b>🎯 Advanced Filtering</b></summary>

Use the powerful query builder to filter your data:

```kotlin
queryBuilder = NotionQueryBuilder()
    .filter {
        // Text filters
        richText { "String: BASE" contains "welcome" } and
        richText { "String: KOR".isNotEmpty } and
        
        // Select/Multi-select filters  
        select { "Status" equals "Published" } and
        multiSelect { "Tags" contains "Mobile" } and
        
        // Boolean filters
        checkBox { "Deprecated" equals false } or
        
        // Combine with OR
        (select { "Priority" equals "High" } or 
         select { "Priority" equals "Medium" })
    }
    .sort {
        // Sort by properties
        property { "Resource ID" by Direction.ASCENDING }
        property { "Priority" by Direction.DESCENDING }
        
        // Sort by timestamps
        timestamp { Timestamp.CREATED_TIME by Direction.DESCENDING }
        timestamp { Timestamp.LAST_EDITED_TIME by Direction.ASCENDING }
    }
```

</details>

---

## 🔑 Getting Notion Credentials

### 1️⃣ Create Integration

1. Go to [🔗 Notion Integrations](https://www.notion.so/my-integrations)
2. Click **"New integration"**
3. Name it and select your workspace
4. Copy the **"Internal Integration Token"**

### 2️⃣ Get Datasource ID

Open your Notion database and copy datasource id:

<details>
<summary>📸 <b>See screenshot</b></summary>

<img width="305" height="759" alt="스크린샷 2025-09-05 오전 11 58 46" src="https://github.com/user-attachments/assets/3310350c-b93c-4172-92cd-4f6ca66738e1" />

</details>

### 3️⃣ Store Securely

Add to your `local.properties`:

```properties
NOTION_API_KEY=your_integration_token_here
DATA_SOURCE_ID=your_database_id_here
```

Then use in `build.gradle.kts`:

```kotlin
val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

stringboard {
    notionApiKey = localProperties.getProperty("NOTION_API_KEY")
    dataSourceId = localProperties.getProperty("DATA_SOURCE_ID")
    // ... other config
}
```

---

## 🎯 Usage

### Generate String Resources

```bash
./gradlew fetchStringboard
```

**This generates:**

- 🇺🇸 `values/strings.xml` (English)
- 🇰🇷 `values-ko/strings.xml` (Korean)
- 🇯🇵 `values-ja/strings.xml` (Japanese)
- 🌍 And all other configured languages...

---

## 🌐 Supported Languages

<details>
<summary><b>View all 35+ supported languages</b></summary>

| Region          | Languages                                                                                                                                                                                                                                                                                                    |
|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **🌏 Asian**    | Korean (ko), Japanese (ja), Chinese Simplified (zh-rCN), Chinese Traditional (zh-rTW), Thai (th), Vietnamese (vi), Hindi (hi), Indonesian (id), Malay (ms), Filipino (fil)                                                                                                                                   |
| **🌍 European** | English (default), Spanish (es), French (fr), German (de), Italian (it), Portuguese (pt), Dutch (nl), Russian (ru), Polish (pl), Czech (cs), Hungarian (hu), Romanian (ro), Croatian (hr), Serbian (sr), Bulgarian (bg), Greek (el), Swedish (sv), Norwegian (no), Danish (da), Finnish (fi), Ukrainian (uk) |
| **🌎 Others**   | Arabic (ar), Hebrew (iw), Turkish (tr), Persian (fa), Swahili (sw), Bengali (bn), Tamil (ta), Telugu (te), Gujarati (gu), Marathi (mr), Punjabi (pa), Urdu (ur)                                                                                                                                              |

</details>

---

## 📋 Requirements

- ✅ Android Gradle Plugin 7.0+
- ✅ Gradle 7.0+
- ✅ Java/Kotlin 17+
- ✅ Active Notion workspace and database

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## ℹ️ Information

- **Notion API version**: `2025-09-03`

## 🔗 Links

- 📦 [**GitHub Repository**](https://github.com/lyh990517/notion-string-automation)
- 🔌 [**Gradle Plugin Portal**](https://plugins.gradle.org/plugin/io.github.lyh990517.notion-stringboard)
- 📚 [**Notion API Documentation**](https://developers.notion.com/)

---

<div align="center">

**Made with ❤️ for Android developers who love automation**

⭐ **Star this repo if it helped you!** ⭐

</div>

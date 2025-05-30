# tk-bank-mock-transaction-app

## О приложении

Тестовое серверное приложение для демонстрации простейших банковских операций.
Условия тестового задания см. в файле "Тестовое задание Java.docx".
Приложение Java, Spring Boot.

## Подготовка к развёртыванию на узле

**Требуемое ПО**:

- PostgreSQL >= 15;
- Java >= 21.

Версии ПО могут быть другими. При разработке используется Postgres 15 и 17 и Java 21: нет никаких явных ограничений на использование других версий ПО.

**В Postgres**:

- предварительно смотрим application.yml;
- создаём БД tk_bank, создаём пользователя "bank_god" с паролем "87654321", отдаём БД tk_bank во владение пользователю bank_god:

```Bash
$ sudo -u postgres psql
postgres=# CREATE DATABASE tk_bank;
CREATE DATABASE
postgres=# CREATE USER bank_god WITH LOGIN PASSWORD '87654321';
CREATE ROLE
postgres=# ALTER DATABASE tk_bank OWNER TO bank_god;
ALTER DATABASE
```

При реальном использовании приложения, при дальнейшей перепубликации, логины и пароли, разумеется, следует скрывать: как минимум их надо просто передавать через параметры командной строки при запуске приложения.

**Java**:

- установить JDK или JRE версии не ниже 21 (на этой версии ведётся разработка: нет никаких явных ограничений на использование других версий Java);
- убедиться в правильности установки, в доступности java, javac, maven (mvn);

## Запуск приложения

Смотри pom.xml. В корне проекта:

```Bash
$mvn clean install
$java -jar target/tk-bank-mock-transaction-0.0.1-SNAPSHOT.jar
```

В браузере:

http://localhost:8092/tk-bank-mock-transaction/swagger-ui/index.html

## Документация

### Swagger -> HTML

redoc-cli был перемещен в состав @redocly/cli.  
Можно использовать npx без установки:

```bash
npx @redocly/cli build-docs <путь-к-openapi-файлу>
```

Генерирование статического HTML из Swagger на примере основного приложения tk-recommendations:

```Bash
curl http://localhost:8090/tk-recommendations/api-docs -o tk-recommendations-api-spec.json
npx @redocly/cli build-docs tk-recommendations-api-spec.json -o tk-recommendations-swagger.html 
```

### JavaDoc -> HTML

```Bash
mvn compile javadoc:javadoc
```

## Другое

### Проверка версий зависимостей

Добавьте плагин в pom.xml:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>versions-maven-plugin</artifactId>
            <version>2.16.2</version>
        </plugin>
    </plugins>
</build>
```

Запустите проверку:

```bash
mvn versions:display-dependency-updates
```

Плагин _versions-maven-plugin_ позволяет обновить версии в pom.xml автоматически:

```bash
mvn versions:use-latest-versions
```

После выполнения проверьте изменения в pom.xml.

**Ограничения**
SNAPSHOT-версии: Плагин _versions-maven-plugin_ игнорирует их по умолчанию.  
Используйте флаг -DallowSnapshots=true, чтобы их включить.

Кастомные репозитории: Если артефакт размещен не в Maven Central, убедитесь, что  
репозиторий добавлен в pom.xml/settings.xml.

### Особенности текущей реализации

Пока есть две версии entity - полные, с каскадированием зависимостей и т.п.,  
и raw-версии - без отношений с другими сущностями. Пока это в процессе.

Обеспечение безопасности операций в условиях многопоточности выполняется с помощью:

- использования атомарных SQL-операций;
- транзакционной обработки на уровне сервиса; при этом нет необходимости в оптимистичных блокировках.

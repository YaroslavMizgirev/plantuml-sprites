# plantuml-sprites

Иконки для схем plantuml

## Список спрайтов plantuml

[sprites list](./sprites-list.md)

## Использование

```plantuml
@startuml Example

!include  https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml
!define DOMESTICICONS https://raw.githubusercontent.com/YaroslavMizgirev/plantuml-sprites/refs/heads/main/sprites/domesticicons
!include DOMESTICICONS/tantor.puml

ContainerDb(tantordb, "Tantor BE", "СУБД", "Это система управления базами данных с объектно-реляционной моделью (ORDBMS), основанная на POSTGRES, версия 4.2, разработанная группой разработчиков PostgreSQL Global Development Group.", $sprite="tantor")

@enduml
```

## Прочие репозитории со спрайтами

[tupadr3](https://github.com/tupadr3/plantuml-icon-font-sprites/tree/main)

[Unofficial PlantUML Standard Library Repositories](https://github.com/plantuml-stdlib/cicon-plantuml-sprites)

[Amazon Web Services - Labs](https://github.com/awslabs/aws-icons-for-plantuml)

Набор спрайтов и макросов по объектам платформы 1С:Предприятие для использования в диаграммах PlantUML: [Dima Ovcharenko](https://github.com/ovcharenko-di/1ce-icons-for-plantuml)

## Build

Sprites are built with provided [script](script/svgsFolderUrl2plantUmlSprites.groovy). To update sprites from icons in Gil Barbara's repo just re-run:

```shell
./svgsFolderUrl2plantUmlSprites.groovy https://github.com/gilbarbara/logos/tree/main/logos
```

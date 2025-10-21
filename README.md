# plantuml-sprites

Иконки для схем plantuml

## Структура папок

[pngs](./pngs/) - Иконки в формате png.

[sprites](./sprites/) - Иконки в формате puml.

[svgs](./svgs/) - Иконки в формате svg.

## Использование

```plantuml
@startuml

!include  https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml
!include https://raw.githubusercontent.com/YaroslavMizgirev/plantuml-sprites/refs/heads/main/sprites/tantor.puml

ContainerDb(tantordb, "Tantor BE", "СУБД", "Это система управления базами данных с объектно-реляционной моделью (ORDBMS), основанная на POSTGRES, версия 4.2, разработанная группой разработчиков PostgreSQL Global Development Group.", $sprite="tantor")

@enduml
```

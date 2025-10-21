# plantuml-sprites

Иконки для схем plantuml

## Структура папок

[/pngs](./pngs/) - Иконки в формате png.

[/sprites](./sprites/) - Иконки в формате puml.

[/svgs](./svgs/) - Иконки в формате svg.

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

# plantuml-sprites

Иконки для схем plantuml

## Список спрайтов plantuml

[sprites list](./sprites-list.md)

## Пример использования

```plantuml
@startuml Example

!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml

!define MYM https://raw.githubusercontent.com/YaroslavMizgirev/plantuml-sprites/refs/heads/main/sprites
!include MYM/1C_company_logo.puml
!include MYM/tantor_logo_main_color.puml
!include MYM/astra_linux_server_logo_black.puml

Title "Схема трехзвенной архитектуры на ОС Astra Linux: Сервер 1С, СУБД Tantor"

Component(astra_linux_server, "Astra Linux Server", "ОС",, "", $sprite="astra_linux_server_logo_black") {
  Boundary(central_server_1C, "Центральный рабочий сервер 1C",,, "") {
    Container(ragent, "ragent", "агент сервера", "Протокол: tcp, tcp6, udp6; Порт: 1540", $sprite="1C_company_logo")
    Container(server_registry, "Реестр сервера", "1cv8wsrv.lst", "Расположен в каталоге данных сервера: список кластеров серверов, в состав которых входит данный рабочий сервер; список администраторов данного рабочего сервера.", $sprite="1C_company_logo")
    ragent -right->> server_registry: "Идентификация центральных серверов кластера"

    Boundary(claster_server_1C, "Кластер серверов",,,, "") {
      Container(rmngr, "rmngr", "главный менеджер кластера", "Протокол: tcp, tcp6, udp, udp6; Порт: 1541", $sprite="1C_company_logo")
      ragent -down->> rmngr: "Управление центральным рабочим сервером"

      Container(claster_registry, "Реестр кластера", "1cv8clst.lst", "Хранится в каталоге данных кластера: список информационных баз, зарегистрированных в данном кластере; список рабочих серверов, входящих в кластер; список рабочих процессов, входящих в кластер; список менеджеров кластера; список сервисов кластера; список администраторов кластера.", $sprite="1C_company_logo")
      rmngr -right->> claster_registry: "Ведение реестра кластера"

      Container(rphost, "rphost", "рабочий процесс", "Протокол: tcp, tcp6, udp, udp6; Порт: 1560-1591", $sprite="1C_company_logo")
      rmngr --down- rphost: ""
    }
  }

  Boundary(serverdb, "Сервер СУБД",,, "") {
    ContainerDb(tantordb, "Tantor BE", "СУБД", "Это система управления базами данных с объектно-реляционной моделью (ORDBMS), основанная на POSTGRES, версия 4.2, разработанная группой разработчиков PostgreSQL Global Development Group.", $sprite="tantor_logo_main_color")
  }
  rphost <<-down->> tantordb: "Взаимодействие с СУБД"
}

SHOW_LEGEND()

@enduml
```

## Прочие репозитории со спрайтами

[tupadr3](https://github.com/tupadr3/plantuml-icon-font-sprites/tree/main)

[Unofficial PlantUML Standard Library Repositories](https://github.com/plantuml-stdlib/cicon-plantuml-sprites)

[Amazon Web Services - Labs](https://github.com/awslabs/aws-icons-for-plantuml)

[Dima Ovcharenko repo](https://github.com/ovcharenko-di/1ce-icons-for-plantuml)

[Gil Barbara repo](https://github.com/gilbarbara/logos)

## Build

Sprites are built with provided [script](script/svgsFolderUrl2plantUmlSprites.groovy). To update sprites from icons in GitHub repo just re-run:

```shell
./svgsFolderUrl2plantUmlSprites.groovy https://github.com/your/path/to/svgs
```

# plantuml-sprites

Иконки для схем plantuml

## Список спрайтов plantuml в этом репозитории

[Sprites list](./sprites-list.md)

## Пример использования

```plantuml
@startuml Example
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Deployment.puml

!define MYM https://raw.githubusercontent.com/YaroslavMizgirev/plantuml-sprites/refs/heads/main/sprites
!include MYM/1C_company_logo.puml
!include MYM/tantor_logo_main_color.puml
!include MYM/astra_linux_server_logo_black.puml
!include MYM/sap_logo_full.puml

' LAYOUT_WITH_LEGEND()

AddElementTag("tantor_db", $bgColor="Orange", $fontColor="Black", $borderColor="Black",,, $sprite="tantor_logo_main_color",, $legendText="СУБД Tantor BE", $legendSprite="", $borderStyle=SolidLine(), $borderThickness="1")
AddElementTag("1c_all", $bgColor="Yellow", $fontColor="Red", $borderColor="Black",, $shape="RoundedBoxShape()", $sprite="1C_company_logo", $techn="", $legendText="Центральный рабочий сервер", $legendSprite="", $borderStyle="SolidLine()", $borderThickness="1")
AddElementTag("sap_all", $bgColor="White", $fontColor="Blue", $borderColor="Black",,, $sprite="sap_logo_full",, $legendText="SAP ERP", $legendSprite="", $borderStyle="SolidLine()", $borderThickness="1")

AddBoundaryTag(claster_process_1c, $legendText="Кластер серверов")

AddNodeTag(astra_linux_server_all, $sprite="astra_linux_server_logo_black", $bgColor="#12a4b8ff", $borderThickness="1", $legendText="OS Astra Linux")
AddNodeTag(process, $bgColor="#71d369ff", $borderThickness="0", $legendText="Процессы")
AddNodeTag(registry_file, $bgColor="#221fd6ff", $fontColor="White", $borderThickness="0", $legendText="Файлы реестра")

Title "Схема трехзвенной архитектуры на ОС Astra Linux: Сервер 1С, СУБД Tantor"

Deployment_Node(astra_linux_server, "", "", "", $tags="astra_linux_server_all") {
  Node(central_server_1C, "Центральный рабочий сервер", "", "", $tags="1c_all") {
    Node(ragent, "ragent", "Агент сервера", "Протокол: tcp, tcp6, udp6; Порт: 1540", $tags="process")
    Node(server_registry, "Реестр сервера", "1cv8wsrv.lst", "Расположен в каталоге данных сервера: список кластеров серверов, в состав которых входит данный рабочий сервер; список администраторов данного рабочего сервера.", $tags="registry_file")
    ragent -right->> server_registry: "Идентификация центральных серверов кластера"

    Boundary(claster_server_1C, "Кластер серверов", $tags="claster_process_1c") {
      Node(rmngr, "rmngr", "Главный менеджер кластера", "Протокол: tcp, tcp6, udp, udp6; Порт: 1541", $tags="process")
      ragent -down->> rmngr: "Управление центральным рабочим сервером"

      Node(claster_registry, "Реестр кластера", "1cv8clst.lst", "Хранится в каталоге данных кластера: список информационных баз, зарегистрированных в данном кластере; список рабочих серверов, входящих в кластер; список рабочих процессов, входящих в кластер; список менеджеров кластера; список сервисов кластера; список администраторов кластера.", $tags="registry_file")
      rmngr -right->> claster_registry: "Ведение реестра кластера"

      Node(rphost, "rphost", "Рабочий процесс", "Протокол: tcp, tcp6, udp, udp6; Порт: 1560-1591", $tags="process")
      rmngr <<--down->> rphost: ""
    }
  }

  Node(sap_erp, "SAP ERP", "", "", $tags="sap_all")
  rphost <<-right->> sap_erp: "HTTP-сервисы"

  ContainerDb(tantordb, "Tantor BE", "СУБД", "Это система управления базами данных с объектно-реляционной моделью (ORDBMS), основанная на POSTGRES, версия 4.2, разработанная группой разработчиков PostgreSQL Global Development Group.", $tags="tantor_db")
  rphost <<-down->> tantordb: "<font color=white>Взаимодействие с СУБД"
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

Cкрипт позволяет из исходного файла svg создать файл png и на его основе спрайт puml, оставляя максимально приближенные размеры к исходному изображению файла svg.

> Usage: svg2png2puml.groovy [options] \<svg file>
>
>-o,--output-dir       Output directory for both PNG and PUML files. Default: current directory
>
>-p,--png-dir          Output directory for PNG files (overrides --output-dir for PNG)
>
>-s,--sprite-name      Name for the sprite (default: derived from SVG filename)
>
>Usage example: `./svg2png2puml.groovy -o sprites/ -p pngs/ -s spritename logo.svg`

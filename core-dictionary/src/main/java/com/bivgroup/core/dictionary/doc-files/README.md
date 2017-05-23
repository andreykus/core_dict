
Описание словарной системы
==========================

Модуль словарной системы условно можно разбить на два:

* генератор метаданных
* типовые методы для работы с сущностями - DAO 


### Генерация метаданных

Исходыми данными для словарной системы являются  метаданные расположенные в БД
Структура БД для словарной системы описана в [файле](..\core-dictionary\docs\Словарная_система.docx). Модель сущностей лежит в [пакете](..\entity)
Для генерации используется [Hibernate Tools](http://hibernate.org/tools/). Исходники [Git](https://github.com/hibernate/hibernate-tools)

#### Принцип работы генератора [GenerateMetadataImpl](..\generator\GenerateMetadataImpl.java)
   Данные из БД начитываются именованным запросом как иерархическая зависимость модулей. Затем производится обход структуры с наложением Посетителей на элементы структуры.
   Посетители:
   
   - [EntityVisitor](..\generator\visitors\EntityVisitor.java) - создает класс. определяется стратегией создания (наследования)
   - [AttributeVisitor](..\generator\visitors\AttributeVisitor.java) - создает совойство в классе. В свою очередь приводится к частным посетителям исходя из типа аттрибута:
       * [LinkVisitor](..\generator\visitors\LinkVisitor.java) - создает свойство ССЫЛКА
       * [FieldVisitor](..\generator\visitors\FieldVisitor.java) - создает свойство КОЛОНКА
       * [ArrayVisitor](..\generator\visitors\ArrayVisitor.java) - создает свойство МНОЖЕСТВО (это отношение OneToMany)
    
   В результате на выходе мы получаем список PersistentClass.
   Данный список мы направляем переработанной [Hibernate Tools](http://hibernate.org/tools/) через фабрику [TypeExport](..\entity\enums\TypeExport.java). 
   Генерация методанных идет по [шаблонам](..\template\templates)  
   В результате на выходе мы получаем метаданные в объекте  [MetadataCollection](..\generator\visitors\util\MetadataCollection.java)
    
   У генератора регистрируются потребители конфигураций наследуемые от ExternalConsumerConfig. Данные с генератора передаются им. Если нет зарегистрированных потребителей данные передаются RuntimeConfig, который обновляет конфигурацию в RUNTIME  
   
### Типовые методы для работы с сущностями 

   Следует разделить на направления:
    
   - простые методы, описаны интерфейсом GenericDAO
   - иерархический тип обработки, описаны интерфейсом ExtendGenericDAO
   
#### Простые методы реализованы в классе [AbstractGenericDAO](..\dao\AbstractGenericDAO.java)

#### Иерархический тип обработки в классе [HierarchyDAO](..\dao\hierarchy\HierarchyDAO.java)

##### Принцип работы генератора иерархического тип обработки
   Этапы:

   - входящая структура разбивется на атомарные операции и перекладывается в граф.
   - последовательное исполенение атомарных операций (insert, update, delete ..)
   [Подробнее описание](HIERARCHY.md)
   
### На данный момент прикладное решение проливается тестами из [файлов](..\\..\\..\\..\\..\\..\\..\\..\src\test\resources\com\bivgroup\db\crm)
  Описание прикладного решения (модели) в [файлах](..\core-dictionary\docs\ver3)
   

    
        
package de.difuture.ekut.pht.station.persistence

import de.difuture.ekut.pht.lib.train.id.ITrainId
import javax.persistence.AttributeConverter

class ITrainIdConverter : AttributeConverter<ITrainId, String> {

    override fun convertToDatabaseColumn(attribute: ITrainId?) = attribute?.repr

    override fun convertToEntityAttribute(dbData: String?) = dbData?.let { ITrainId.of(it)}
}

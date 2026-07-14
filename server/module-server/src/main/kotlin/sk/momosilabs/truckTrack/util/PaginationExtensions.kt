package sk.momosilabs.truckTrack.util

import com.momosi.trucktrack.shared.common.PageDto
import com.momosi.trucktrack.shared.common.PageableDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order

inline fun <T : Any, U> Page<T>.toDto(transform: (T) -> U): PageDto<U> =
    PageDto(
        totalElements = totalElements,
        totalPages = totalPages,
        number = number,
        size = size,
        numberOfElements = numberOfElements,
        content = content.map { transform.invoke(it) },
    )

fun PageableDto.toModel(): Pageable {
    if (sort.isNullOrBlank())
        return PageRequest.of(page, size)
    return PageRequest.of(page, size, sort.toSortModel())
}

fun String?.toSortModel(): Sort {
    if (isNullOrBlank()) return Sort.by(Order.desc("id"))
    val sortByColumns = this!!.split(";").map { col ->
        col.split(",").let { if (it[1] == "desc") Order.desc(it[0]) else Order.asc(it[0]) }
    }
    return Sort.by(sortByColumns)
}

const deleteImage = (element) => {
    const imgId = element.id
    fetch(`/uploads/delete/${imgId}`, {
        method: 'DELETE'
    }).then(responese => {
        if (!responese.ok) {
            console.error(responese.error())
        }
        document.location.reload()
    })
}
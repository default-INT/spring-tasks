const statusSelector = {
    'ACTIVE': 'BANNED',
    'BANNED': 'ACTIVE',
}

function changeStatus(element) {
    fetch('/api/auth/change-status', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            username: element.id
        })
    }).then(response => {
        if (!response.ok) {
            console.error(response.error)
            return;
        }
        return response.text();
    }).then(response => {
        console.info(response)
        const status = element.innerText;
        const newStatus = statusSelector[status.toUpperCase()]
        element.classList.remove(status.toLowerCase())
        element.classList.add(newStatus.toLowerCase())
        element.innerText = newStatus[0] + newStatus.toLowerCase().slice(1);
    })
}
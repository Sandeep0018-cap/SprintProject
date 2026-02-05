// Lightweight renderer for the dashboard charts.
// Exposes window.renderDashboardCharts(data) where data is the object filled by Thymeleaf.

(function() {
  function renderPie(ctx, labels, values, options){
    if (!ctx) return;
    return new Chart(ctx, {
      type: 'pie',
      data: {
        labels: labels,
        datasets: [{
          data: values,
          backgroundColor: [
            'rgba(14,165,164,0.95)',
            'rgba(20,184,166,0.95)',
            'rgba(34,197,94,0.95)',
            'rgba(59,130,246,0.95)',
            'rgba(239,68,68,0.95)'
          ],
          borderColor: 'rgba(0,0,0,0.15)',
          borderWidth: 1
        }]
      },
      options: Object.assign({
        plugins: { legend: { labels: { color: '#cbd5e1' } } }
      }, options || {})
    });
  }

  function renderLine(ctx, labels, values, options){
    if (!ctx) return;
    return new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Revenue',
          data: values,
          fill: true,
          backgroundColor: 'rgba(14,165,164,0.12)',
          borderColor: 'rgba(14,165,164,0.95)',
          pointBackgroundColor: '#fff',
          tension: 0.3
        }]
      },
      options: Object.assign({
        scales: {
          x: { ticks: { color: '#9aa4b2' } },
          y: { ticks: { color: '#9aa4b2' } }
        },
        plugins: { legend: { display: false } }
      }, options || {})
    });
  }

  window.renderDashboardCharts = function(data){
    try {
      const orderCtx = document.getElementById('orderStatusPie');
      if (orderCtx) renderPie(orderCtx, data.orderStatusLabels || [], data.orderStatusValues || []);

      const salesCtx = document.getElementById('salesLine');
      if (salesCtx) renderLine(salesCtx, data.monthsLabels || [], data.monthsValues || []);

      const brandEl = document.getElementById('brandBar');
      if (brandEl && data.brandLabels) {
        new Chart(brandEl, {
          type: 'bar',
          data: {
            labels: data.brandLabels,
            datasets: [{
              label: 'Revenue by Brand',
              data: data.brandValues || [],
              backgroundColor: 'rgba(14,165,164,0.9)'
            }]
          },
          options: {
            plugins: { legend: { display: false } },
            scales: { x: { ticks: { color: '#9aa4b2' } }, y: { ticks: { color: '#9aa4b2' } } }
          }
        });
      }

      const stockEl = document.getElementById('stockDonut');
      if (stockEl) {
        renderPie(stockEl, ['Low','Limited','Healthy'], [data.stockDonut.red||0,data.stockDonut.orange||0,data.stockDonut.green||0]);
      }
    } catch (e) {
      console.error('Error rendering dashboard charts: ', e);
    }
  };
})();
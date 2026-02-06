// NEO 3D Effects bootstrapper

// Initialize VanillaTilt if available
(function initTilt(){
  if (window.VanillaTilt) {
    VanillaTilt.init(document.querySelectorAll(".tilt"), {
      max: 10,
      speed: 500,
      glare: true,
      "max-glare": 0.2,
      scale: 1.01,
      perspective: 1000
    });
  }
})();

// Lightweight parallax on scroll for elements with data-parallax
(function parallax(){
  const els = Array.from(document.querySelectorAll("[data-parallax]"));
  if (!els.length) return;
  const onScroll = () => {
    const y = window.scrollY || 0;
    els.forEach(el => {
      const depth = parseFloat(el.getAttribute("data-parallax")) || 0.2;
      el.style.transform = `translate3d(0, ${y * depth * -1}px, 0)`;
    });
  };
  onScroll(); window.addEventListener("scroll", onScroll, { passive:true });
})();

// View switcher (Category Detail)
(function viewSwitch(){
  const container = document.querySelector("[data-view-container]");
  if (!container) return;
  const cards = container.querySelector("#cardsView");
  const table = container.querySelector("#tableView");
  const buttons = container.querySelectorAll("[data-view-btn]");
  const setView = (mode) => {
    if (mode === "cards"){ cards?.classList.remove("d-none"); table?.classList.add("d-none"); }
    else { table?.classList.remove("d-none"); cards?.classList.add("d-none"); }
    buttons.forEach(b => b.classList.toggle("active", b.dataset.viewBtn === mode));
  };
  buttons.forEach(b => b.addEventListener("click", () => setView(b.dataset.viewBtn)));
  // default to cards on first load
  setView("cards");
})();
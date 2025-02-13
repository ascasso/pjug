package io.bootify.simple5.controller;

import io.bootify.simple5.domain.BallInfo;
import io.bootify.simple5.model.BallDetailDTO;
import io.bootify.simple5.repos.BallInfoRepository;
import io.bootify.simple5.service.BallDetailService;
import io.bootify.simple5.util.CustomCollectors;
import io.bootify.simple5.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/ballDetails")
public class BallDetailController {

    private final BallDetailService ballDetailService;
    private final BallInfoRepository ballInfoRepository;

    public BallDetailController(final BallDetailService ballDetailService,
            final BallInfoRepository ballInfoRepository) {
        this.ballDetailService = ballDetailService;
        this.ballInfoRepository = ballInfoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("ballInfoValues", ballInfoRepository.findAll(Sort.by("ballId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(BallInfo::getBallId, BallInfo::getBallName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("ballDetails", ballDetailService.findAll());
        return "ballDetail/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("ballDetail") final BallDetailDTO ballDetailDTO) {
        return "ballDetail/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("ballDetail") @Valid final BallDetailDTO ballDetailDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ballDetail/add";
        }
        ballDetailService.create(ballDetailDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ballDetail.create.success"));
        return "redirect:/ballDetails";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("ballDetail", ballDetailService.get(id));
        return "ballDetail/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("ballDetail") @Valid final BallDetailDTO ballDetailDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ballDetail/edit";
        }
        ballDetailService.update(id, ballDetailDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ballDetail.update.success"));
        return "redirect:/ballDetails";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        ballDetailService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("ballDetail.delete.success"));
        return "redirect:/ballDetails";
    }

}
